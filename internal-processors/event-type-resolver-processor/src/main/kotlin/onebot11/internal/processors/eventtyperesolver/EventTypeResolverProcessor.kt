/*
 * Copyright (c) 2024. ForteScarlet.
 *
 * This file is part of simbot-component-onebot.
 *
 * simbot-component-onebot is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-onebot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-onebot.
 * If not, see <https://www.gnu.org/licenses/>.
 */
@file:Suppress("PackageNaming")

package onebot11.internal.processors.eventtyperesolver

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.jvm.jvmMultifileClass
import com.squareup.kotlinpoet.jvm.jvmName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.KClass


/**
 *
 * @author ForteScarlet
 */
class EventTypeResolverProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        EventTypeResolverProcessor(environment)
}

private val KSerializerClassName =
    ClassName("kotlinx.serialization", "KSerializer")

private const val EVENT_BASE_PACKAGE = "love.forte.simbot.component.onebot.v11.event"

private val EventClassName =
    ClassName(EVENT_BASE_PACKAGE, "Event")

private val ExpectEventTypeAnnotationClassName =
    ClassName(EVENT_BASE_PACKAGE, "ExpectEventType")

// postType
// subType

private const val FUNCTION_POST_TYPE_PARAM_NAME = "postType"
private const val FUNCTION_SUB_TYPE_PARAM_NAME = "subType"

private const val ANNOTATION_POST_TYPE_ARG_NAME = "postType"
private const val ANNOTATION_SUB_TYPE_ARG_NAME = "subType"

// ktx 可序列化标记
private const val SERIALIZABLE_ANNOTATION = "kotlinx.serialization.Serializable"

private const val FUNCTION_RESOLVER_SERIALIZER_NAME = "resolverEventSerializer"
private const val FUNCTION_RESOLVER_TYPE_NAME = "resolverEventType"

private const val FILE_NAME = "EventResolver.generated"
private const val FILE_JVM_NAME = "EventResolvers"

private val InternalSimbotAPIClassName = ClassName("love.forte.simbot.annotations", "InternalSimbotAPI")

private class EventTypeResolverProcessor(val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    private val generated = AtomicBoolean(false)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (!generated.compareAndSet(false, true)) {
            environment.logger.warn("Processor was used.")
            return emptyList()
        }

        val eventDeclaration = resolver.getClassDeclarationByName(EventClassName.canonicalName)
            ?: throw NoSuchElementException("Class: $EventClassName")

        val expectEventTypeAnnotationDeclaration =
            resolver.getClassDeclarationByName(ExpectEventTypeAnnotationClassName.canonicalName)
                ?: throw NoSuchElementException("Class: $ExpectEventTypeAnnotationClassName")

        val eventType = eventDeclaration.asStarProjectedType()
        val expectEventTypeAnnotationType = expectEventTypeAnnotationDeclaration.asStarProjectedType()

        val allEventImplWithAnnotation = resolver.getSymbolsWithAnnotation(SERIALIZABLE_ANNOTATION)
            .filterIsInstance<KSClassDeclaration>()
            // 是一个可序列化的具体的类
            .filter { !it.isAbstract() }
            // 是 event 的子类
            .filter { eventType.isAssignableFrom(it.asStarProjectedType()) }
            // 有注解 ExpectType
            .filter {
                it.annotations.any { a -> expectEventTypeAnnotationType.isAssignableFrom(a.annotationType.resolve()) }
            }
            .onEach {
                environment.logger.info("Resolved message element impl: $it", it)
            }
            .toList()

        val values = resolveTypes(expectEventTypeAnnotationType, allEventImplWithAnnotation)

        val resolveSerializerFunction = resolveSerializerFunction(values)
        val resolveTypeFunction = resolveTypeFunction(values)

        val generatedFile = FileSpec.builder(EVENT_BASE_PACKAGE, FILE_NAME).apply {
            addFileComment(
                """
                ****************************
                此文件内容是 **自动生成** 的
                ****************************
                """.trimIndent()
            )
            addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .addMember("%S, %S", "ALL", "unused")
                    .build()
            )

            jvmName(FILE_JVM_NAME)
            jvmMultifileClass()

            addFunction(resolveSerializerFunction)
            addFunction(resolveTypeFunction)

            indent("    ")
        }.build()

        generatedFile.writeTo(
            codeGenerator = environment.codeGenerator,
            aggregating = true,
            originatingKSFiles = buildList {
                eventDeclaration.containingFile?.also(::add)
                for (declaration in allEventImplWithAnnotation) {
                    declaration.containingFile?.also(::add)
                }
            }
        )

        return emptyList()
    }


    private fun resolveTypes(
        eventType: KSType,
        allEventImplWithAnnotation: List<KSClassDeclaration>
    ): Map<String, Map<String, KSClassDeclaration>> {
        // Map<String, Map<String, ClassDeclaration>>
        val result = mutableMapOf<String, MutableMap<String, KSClassDeclaration>>()

        for (declaration in allEventImplWithAnnotation) {
            val expectTypeAnnotation =
                declaration.annotations.find { eventType.isAssignableFrom(it.annotationType.resolve()) } ?: continue

            val postType =
                expectTypeAnnotation.arguments
                    .find {
                        it.name?.asString() == ANNOTATION_POST_TYPE_ARG_NAME
                    }?.value as String?
            val subType =
                expectTypeAnnotation.arguments
                    .find {
                        it.name?.asString() == ANNOTATION_SUB_TYPE_ARG_NAME
                    }?.value as String?

            if (postType == null) {
                environment.logger.warn(
                    "Cannot get `postType` from the @ExpectEventType annotation on $declaration",
                    declaration
                )
                continue
            }
            if (subType == null) {
                environment.logger.warn(
                    "Cannot get `subType` from the @ExpectEventType annotation on $declaration",
                    declaration
                )
                continue
            }


            result.computeIfAbsent(postType) { mutableMapOf() }[subType] = declaration
        }

        return result
    }


    private inline fun resolveValueStatements(
        values: Map<String, Map<String, KSClassDeclaration>>,
        beginPostType: (postType: String, subMap: Map<String, KSClassDeclaration>) -> Unit,
        onStatement: (postType: String, subType: String, decl: KSClassDeclaration) -> Unit,
        endPostType: (postType: String, subMap: Map<String, KSClassDeclaration>) -> Unit,
    ) {
        for ((postType, subMap) in values) {
            beginPostType(postType, subMap)
            for ((subType, declaration) in subMap) {
                onStatement(postType, subType, declaration)
            }
            endPostType(postType, subMap)
        }
    }

    private inline fun CodeBlock.Builder.resolveCodeBlockWhenStatements(
        values: Map<String, Map<String, KSClassDeclaration>>,
        postTypeValName: String = "postType",
        subTypeValName: String = "subType",
        onStatement: (postType: String, subType: String, decl: KSClassDeclaration) -> Unit,
        onPostTypeElse: () -> Unit,
        onSubTypeElse: () -> Unit,
    ) {
        beginControlFlow("when(%L)", postTypeValName)
        resolveValueStatements(
            values,
            beginPostType = { postType, _ ->
                add("%S -> ", postType)
                beginControlFlow("when(%L)", subTypeValName)
            },
            onStatement = { postType, subType, declaration ->
                add("%S -> ", subType)
                onStatement(postType, subType, declaration)
            },
            endPostType = { _, _ ->
                onSubTypeElse()
                endControlFlow()
            },
        )
        onPostTypeElse()
        endControlFlow()
    }


    private fun FunSpec.Builder.resolveKDoc(
        values: Map<String, Map<String, KSClassDeclaration>>
    ) {
        resolveValueStatements(
            values,
            beginPostType = { postType, subMap ->
                addKdoc("- `%S` (total: %L) \n", postType, subMap.size)
            },
            onStatement = { _, subType, declaration ->
                addKdoc("    - `%S` -> [%T]\n", subType, declaration.toClassName())
            },
            endPostType = { _, _ ->
            }
        )
    }

    private fun resolveSerializerFunction(
        values: Map<String, Map<String, KSClassDeclaration>>
    ): FunSpec {
        return FunSpec.builder(FUNCTION_RESOLVER_SERIALIZER_NAME).apply {
            addAnnotation(InternalSimbotAPIClassName)
            addParameter(FUNCTION_POST_TYPE_PARAM_NAME, STRING)
            addParameter(FUNCTION_SUB_TYPE_PARAM_NAME, STRING)
            returns(
                KSerializerClassName.parameterizedBy(
                    // out Event
                    WildcardTypeName.producerOf(EventClassName)
                ).copy(nullable = true)
            )

            resolveKDoc(values)

            addCode(
                buildCodeBlock {
                    add("return ")
                    resolveCodeBlockWhenStatements(
                        values,
                        postTypeValName = FUNCTION_POST_TYPE_PARAM_NAME,
                        subTypeValName = FUNCTION_SUB_TYPE_PARAM_NAME,
                        onStatement = { _, _, declaration ->
                            addStatement("%T.serializer()", declaration.toClassName())
                        },
                        onPostTypeElse = { addStatement("else -> null") },
                        onSubTypeElse = { addStatement("else -> null") },
                    )
                }
            )
        }.build()
    }

    private fun resolveTypeFunction(
        values: Map<String, Map<String, KSClassDeclaration>>
    ): FunSpec {
        return FunSpec.builder(FUNCTION_RESOLVER_TYPE_NAME).apply {
            addAnnotation(InternalSimbotAPIClassName)
            addParameter(FUNCTION_POST_TYPE_PARAM_NAME, STRING)
            addParameter(FUNCTION_SUB_TYPE_PARAM_NAME, STRING)
            returns(
                KClass::class.asTypeName().parameterizedBy(
                    // out Event
                    WildcardTypeName.producerOf(EventClassName)
                ).copy(nullable = true)
            )

            resolveKDoc(values)

            addCode(
                buildCodeBlock {
                    add("return ")
                    resolveCodeBlockWhenStatements(
                        values,
                        postTypeValName = FUNCTION_POST_TYPE_PARAM_NAME,
                        subTypeValName = FUNCTION_SUB_TYPE_PARAM_NAME,
                        onStatement = { _, _, declaration ->
                            addStatement("%T::class", declaration.toClassName())
                        },
                        onPostTypeElse = { addStatement("else -> null") },
                        onSubTypeElse = { addStatement("else -> null") },
                    )
                }
            )
        }.build()
    }

}
