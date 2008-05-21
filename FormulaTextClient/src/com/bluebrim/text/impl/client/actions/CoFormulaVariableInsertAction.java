package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.text.*;

import com.bluebrim.formula.shared.*;

public class CoFormulaVariableInsertAction extends TextAction
{

    public static final String insertFormulaVariableAction = "insert-formula-variable-action";

    protected JTextComponent m_editor;

    public CoFormulaVariableInsertAction(JTextComponent editor)
    {
        super(insertFormulaVariableAction + editor.toString());
        m_editor = editor;
    }

    public void actionPerformed(ActionEvent e)
    {
        if (!m_editor.isEditable()) return;

        String name = (String) e.getActionCommand();
        if (name == null) return;

        CoFormulaStyledDocumentIF doc = (CoFormulaStyledDocumentIF) m_editor.getDocument();
        doc.insertFormulaVariable(m_editor.getSelectionStart(), m_editor.getSelectionEnd() - m_editor.getSelectionStart(), name);
        m_editor.repaint();
    }
}