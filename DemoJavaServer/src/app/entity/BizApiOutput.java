/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.entity;

import core.controller.ApiOutput;

/**
 *
 * @author tamnnq
 */
public class BizApiOutput extends ApiOutput {

    public static enum ERROR_CODE_API {
        SUCCESS(1, "SUCCESS"),
        INVALID_DATA_INPUT(-1, "INVALID_INPUT"),
        UNSUPPORTED_ERROR(-2, "UNSUPPORTED_ERROR"),
        ACTION_INVALID(-3, "ACTION_INVALID"),
        SERVER_ERROR(-503, "SYSTEM_ERROR"),;

        public int code;
        public String message;

        private ERROR_CODE_API(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    public BizApiOutput(ERROR_CODE_API result) {
        super(result.code, result.message, null);
    }

    public BizApiOutput(int code, String message, Object data) {
        super(code, message, data);
    }

    public BizApiOutput(ERROR_CODE_API result, Object data) {
        super(result.code, result.message, data);
    }

    /*
        API OUTPUT DEFINE
     */
}
