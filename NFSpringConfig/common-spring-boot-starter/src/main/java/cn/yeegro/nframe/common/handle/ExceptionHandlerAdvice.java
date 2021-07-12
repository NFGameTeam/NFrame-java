package cn.yeegro.nframe.common.handle;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import cn.yeegro.nframe.common.exception.controller.ControllerException;
import cn.yeegro.nframe.common.exception.dao.DataAccessException;
import cn.yeegro.nframe.common.exception.hystrix.HystrixException;
import cn.yeegro.nframe.common.exception.service.ServiceException;

/**
 * @author 作者 zoocee(改)
 * @version 创建时间：2017年11月12日 上午22:57:51 异常通用处理
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {

	/**
	 * IllegalArgumentException异常处理返回json 状态码:400
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({ IllegalArgumentException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> badRequestException(IllegalArgumentException exception) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.BAD_REQUEST.value());
		data.put("msg", exception.getMessage());

		return data;
	}

	
	/**
	 * AccessDeniedException异常处理返回json 状态码:403
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({ AccessDeniedException.class })
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Map<String, Object> badMethodExpressException(AccessDeniedException exception) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.FORBIDDEN.value());
		data.put("msg", exception.getMessage());

		return data;
	}
@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleError(MissingServletRequestParameterException e) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.BAD_REQUEST.value());
		data.put("msg", e.getMessage());

		return data;
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleError(MethodArgumentTypeMismatchException e) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.BAD_REQUEST.value());
		data.put("msg", e.getMessage());

		return data;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleError(MethodArgumentNotValidException e) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.BAD_REQUEST.value());
		data.put("msg", e.getMessage());

		return data;
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleError(BindException e) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.BAD_REQUEST.value());
		data.put("msg", e.getMessage());

		return data;
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleError(ConstraintViolationException e) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.BAD_REQUEST.value());
		data.put("msg", e.getMessage());

		return data;
	}
	

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> handleError(NoHandlerFoundException e) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
		data.put("msg", e.getMessage());

		return data;
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> handleError(HttpMessageNotReadableException e) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
		data.put("msg", e.getMessage());

		return data;
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public Map<String, Object> handleError(HttpRequestMethodNotSupportedException e) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.METHOD_NOT_ALLOWED.value());
		data.put("msg", e.getMessage());

		return data;
	}
	
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Map<String, Object> handleError(HttpMediaTypeNotSupportedException e) {
    	Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
		data.put("msg", e.getMessage());
		return data;
    }
	 


	@ExceptionHandler({ DataAccessException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> dataAccessException(DataAccessException exception) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
		data.put("msg", exception.getMessage());

		return data;

	}

	@ExceptionHandler({ ServiceException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> serviceException(ServiceException exception) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
		data.put("msg", exception.getMessage());

		return data;
	}

	@ExceptionHandler({ ControllerException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> controllerException(ControllerException exception) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
		data.put("msg", exception.getMessage());

		return data;
	}

	@ExceptionHandler({ HystrixException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> hytrixException(HystrixException exception) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
		data.put("msg", exception.getMessage());

		return data;
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> handleError(Throwable e) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
		data.put("msg", e.getMessage());

		return data;
	}
}
