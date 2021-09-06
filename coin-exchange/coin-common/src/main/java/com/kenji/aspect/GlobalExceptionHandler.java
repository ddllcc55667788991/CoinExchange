package com.kenji.aspect;

import com.baomidou.mybatisplus.extension.api.IErrorCode;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.kenji.model.R;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author Kenji
 * @Date 2021/8/16 18:57
 * @Description
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 内部api调用的异常处理
     * @param exception
     * @return
     */
    @ExceptionHandler(ApiException.class)
    public R apiHandlerExceptin(ApiException exception){
        IErrorCode errorCode = exception.getErrorCode();
        if (errorCode != null){
            return R.fail(errorCode.getCode());
        }
        return R.fail(exception.getMessage());
    }

    /**
     * 方法参数检验异常
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R methodAgrsNotValidateHandlerException(MethodArgumentNotValidException exception){
        BindingResult bindingResult = exception.getBindingResult();
        if (bindingResult != null){
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError!=null){
            return R.fail(fieldError.getField()+fieldError.getDefaultMessage());
            }
        }
        return R.fail(exception.getMessage());
    }

    /**
     * 对象内部使用Validate,没有校验成功的异常
     * @param exception
     * @return
     */
    @ExceptionHandler(BindException.class)
    public R BindHandlerException(BindException exception){
        BindingResult bindingResult = exception.getBindingResult();
        if (bindingResult !=null){
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError!=null){
                return R.fail(fieldError.getField()+fieldError.getDefaultMessage());
            }
        }
        return R.fail(exception.getMessage());
    }
}
