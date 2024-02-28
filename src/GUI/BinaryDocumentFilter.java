package GUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
/**
 * The BinaryDocumentFilter class is to limit Binary inputField in the Front End GUI
 */
public class BinaryDocumentFilter extends DocumentFilter {
    private int maxLength;

    public BinaryDocumentFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) {
            return;
        }
        if (isBinary(string) && (fb.getDocument().getLength() + string.length()) <= maxLength) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text == null) {
            return;
        }
        if (isBinary(text) && (fb.getDocument().getLength() + text.length() - length) <= maxLength) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean isBinary(String text) {
        return text.matches("[01]*");
    }
}
