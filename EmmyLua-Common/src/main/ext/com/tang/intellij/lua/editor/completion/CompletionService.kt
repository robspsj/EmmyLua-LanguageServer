package com.tang.intellij.lua.editor.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.completion.impl.CamelHumpMatcher
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.lang.PsiBuilderFactory
import com.intellij.psi.PsiFile
import com.intellij.util.Consumer
import com.tang.intellij.lua.IConfiguration
import com.tang.intellij.lua.lang.LuaParserDefinition
import com.tang.intellij.lua.lexer.LuaLexer
import com.tang.intellij.lua.parser.LuaParser
import com.tang.intellij.lua.psi.LuaPsiFile

class CompletionResultSetImpl(private val consumer: Consumer<LookupElement>) : CompletionResultSet() {
    override fun withPrefixMatcher(prefix: String): CompletionResultSet {
        val set = CompletionResultSetImpl(consumer)
        set.prefixMatcher = prefixMatcher.cloneWithPrefix(prefix)
        return set
    }

    private val set = mutableSetOf<String>()
    override fun addElement(item: LookupElement) {
        if (set.add(item.lookupString))
            consumer.consume(item)
    }
}

object CompletionService {
    private val contributor = LuaCompletionContributor()

    private val docContributor = LuaDocCompletionContributor()

    fun collectCompletion(psi: PsiFile, pos: Int, config: IConfiguration, consumer: Consumer<LookupElement>) {
        //val element = psi.findElementAt(pos)
        val text = psi.text.replaceRange(pos, pos, "emmy")

        val parser = LuaParser()
        val builder = PsiBuilderFactory.getInstance().createBuilder(LuaParserDefinition(), LuaLexer(), text)
        val node = parser.parse(LuaParserDefinition.FILE, builder)
        val tempPsi = node.psi as LuaPsiFile
        tempPsi.virtualFile = psi.virtualFile
        val position = tempPsi.findElementAt(pos)

        val parameters = CompletionParameters()
        parameters.completionType = CompletionType.BASIC
        parameters.position = position!!
        parameters.originalFile = psi
        parameters.offset = pos

        val result = CompletionResultSetImpl(consumer)
        val prefix = findPrefix(text, pos)
        result.prefixMatcher = CamelHumpMatcher(prefix, config.completionCaseSensitive)

        parameters.originalFile.putUserData(CompletionSession.KEY, CompletionSession(parameters, result))

        contributor.fillCompletionVariants(parameters, result)
        docContributor.fillCompletionVariants(parameters, result)
    }

    private fun findPrefix(text: String, pos: Int): String {
        var i = pos
        while (i > 0) {
            val c = text[i - 1]
            if (!c.isJavaIdentifierPart() && !c.isJavaIdentifierStart()) {
                break
            }
            i--
        }
        return text.substring(i, pos)
    }
}