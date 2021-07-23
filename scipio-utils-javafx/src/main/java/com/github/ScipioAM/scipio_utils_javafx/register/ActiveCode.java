package com.github.ScipioAM.scipio_utils_javafx.register;

/**
 * 激活码 javaBean
 * @author Alan Min
 * @since 2021/1/15
 */
public class ActiveCode {

    private RegisterMode mode;

    private String signature;

    private Long data;

    private String plaintext;

    public ActiveCode() { }

    public ActiveCode(RegisterMode mode, String signature) {
        this.mode = mode;
        this.signature = signature;
    }

    public ActiveCode(RegisterMode mode, String signature, Long data) {
        this.mode = mode;
        this.signature = signature;
        this.data = data;
    }

    public RegisterMode getMode() {
        return mode;
    }

    public void setMode(RegisterMode mode) {
        this.mode = mode;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }

    public String getPlaintext() {
        return plaintext;
    }

    public void setPlaintext(String plaintext) {
        this.plaintext = plaintext;
    }
}
