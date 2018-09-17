/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controller;

import app.entity.BizApiOutput;
import app.entity.BizApiOutput.ERROR_CODE_API;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import core.controller.ApiOutput;
import core.controller.ApiServlet;
import core.utilities.CommonUtil;
import app.service.DemoService;

/**
 *
 * @author tamnnq
 */
public class DemoApiController extends ApiServlet {

    @Override
    protected ApiOutput execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String action = req.getParameter("action");
            if (!CommonUtil.isValidString(action)) {
                return new BizApiOutput(ERROR_CODE_API.ACTION_INVALID);
            }
            switch (action) {
                case "plus":
                    return plusService(req, resp);
                case "mult":
                    return multService(req, resp);
                default:
                    return new BizApiOutput(ERROR_CODE_API.UNSUPPORTED_ERROR);
            }
        } catch (Exception e) {
            return new BizApiOutput(ERROR_CODE_API.SERVER_ERROR);
        }
    }

    private ApiOutput plusService(HttpServletRequest req, HttpServletResponse resp) {
        if (!checkValidParam(req, new String[]{"number1", "number2"})
                || !CommonUtil.isInteger(req.getParameter("number1"))
                || !CommonUtil.isInteger(req.getParameter("number2"))) {
            return new BizApiOutput(ERROR_CODE_API.INVALID_DATA_INPUT);
        }
        int number1 = Integer.parseInt(req.getParameter("number1"));
        int number2 = Integer.parseInt(req.getParameter("number2"));

        int result = DemoService.getInstance().PlusService(number1, number2);
        return new BizApiOutput(ERROR_CODE_API.SUCCESS, result);
    }

    private ApiOutput multService(HttpServletRequest req, HttpServletResponse resp) {
        if (!checkValidParam(req, new String[]{"number1", "number2"})
                || !CommonUtil.isInteger(req.getParameter("number1"))
                || !CommonUtil.isInteger(req.getParameter("number2"))) {
            return new BizApiOutput(ERROR_CODE_API.INVALID_DATA_INPUT);
        }
        int number1 = Integer.parseInt(req.getParameter("number1"));
        int number2 = Integer.parseInt(req.getParameter("number2"));

        int result = DemoService.getInstance().MultService(number1, number2);
        return new BizApiOutput(ERROR_CODE_API.SUCCESS, result);
    }
}
