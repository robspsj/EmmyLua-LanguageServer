package com.tang.vscode.utils

import com.intellij.psi.*

object TargetElementUtil {
    fun findTarget(file: PsiFile?, pos: Int): PsiElement? {
        file ?: return null

        val element = file.findElementAt(pos)
        var cur = element
        if (cur is PsiWhiteSpace)
            cur = file.findElementAt(pos - 1)

        while (cur != null && cur !is PsiFile) {
            if (cur is PsiNamedElement) {
                var id = cur
                if (cur is PsiNameIdentifierOwner) {
                    id = cur.nameIdentifier
                }
                if (id != null && id.node.startOffset <= pos && id.node.startOffset + id.node.textLength >= pos) {
                    return cur
                }
            }
            // reference
            val reference = cur.reference
            if (reference != null) {
                var textRange = reference.rangeInElement
                val parentRange = reference.element.textRange
                textRange = textRange.shiftRight(parentRange.startOffset)
                if (textRange.contains(pos))
                    return cur
            }
            cur = cur.parent
        }

        return null
    }
}