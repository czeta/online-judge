package com.czeta.onlinejudge.util.utils;

import com.czeta.onlinejudge.util.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.util.exception.APIRuntimeException;
import com.czeta.onlinejudge.util.response.IBaseResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.MessageFormatter;

import java.util.Collection;
import java.util.Map;

public class AssertUtils {
    /**<==null判定 begin==>**/
    public static void isNull(Object obj, String message) {
        if (obj != null) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
        }
    }

    public static void isNull(Object obj, String message, Object... args) {
        if (obj != null) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR,
                    MessageFormatter.arrayFormat(message, args).getMessage());
        }
    }

    public static void isNull(Object obj, Integer status, String message) {
        if (obj != null) {
            throw new APIRuntimeException(status, message);
        }
    }

    public static void isNull(Object obj, IBaseStatusMsg statusMsg) {
        if (obj != null) {
            throw new APIRuntimeException(statusMsg);
        }
    }

    public static void isNull(Object obj, IBaseStatusMsg statusMsg, String message) {
        if (obj != null) {
            throw new APIRuntimeException(statusMsg, message);
        }
    }
    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
        }
    }

    public static void notNull(Object obj, String message, Object... args) {
        if (obj == null) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR,
                    MessageFormatter.arrayFormat(message, args).getMessage());
        }
    }

    public static void notNull(Object obj, Integer status, String message) {
        if (obj == null) {
            throw new APIRuntimeException(status, message);
        }
    }

    public static void notNull(Object obj, IBaseStatusMsg statusMsg) {
        if (obj == null) {
            throw new APIRuntimeException(statusMsg);
        }
    }

    public static void notNull(Object obj, IBaseStatusMsg statusMsg, String message) {
        if (obj == null) {
            throw new APIRuntimeException(statusMsg, message);
        }
    }
    /**<==null判定 end==>**/
    /**<==true判定 begin==>**/
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
        }
    }
    public static void isTrue(boolean expression, String message, Object... args) {
        if (!expression) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR,
                    MessageFormatter.arrayFormat(message, args).getMessage());
        }
    }
    public static void isTrue(boolean expression, Integer status, String message) {
        if (!expression) {
            throw new APIRuntimeException(status, message);
        }
    }
    public static void isTrue(boolean expression, IBaseStatusMsg statusMsg) {
        if (!expression) {
            throw new APIRuntimeException(statusMsg);
        }
    }
    public static void isTrue(boolean expression, IBaseStatusMsg statusMsg, String message) {
        if (!expression) {
            throw new APIRuntimeException(statusMsg, message);
        }
    }
    /**<==true判定 end==>**/
    /**<==empty判定 begin==>**/
    public static <T> void notEmpty(Collection<T> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
        }
    }
    public static <T> void notEmpty(Collection<T> collection, Integer status, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new APIRuntimeException(status, message);
        }
    }
    public static <T> void notEmpty(Collection<T> collection, IBaseStatusMsg statusMsg) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new APIRuntimeException(statusMsg);
        }
    }
    public static <T> void notEmpty(Collection<T> collection, IBaseStatusMsg statusMsg, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new APIRuntimeException(statusMsg, message);
        }
    }

    public static <K, V> void notEmpty(Map<K, V> map, String message) {
        if (MapUtils.isEmpty(map)) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
        }
    }
    public static <K, V> void notEmpty(Map<K, V> map, Integer status, String message) {
        if (MapUtils.isEmpty(map)) {
            throw new APIRuntimeException(status, message);
        }
    }
    public static <K, V> void notEmpty(Map<K, V> map, IBaseStatusMsg statusMsg) {
        if (MapUtils.isEmpty(map)) {
            throw new APIRuntimeException(statusMsg);
        }
    }
    public static <K, V> void notEmpty(Map<K, V> map, IBaseStatusMsg statusMsg, String message) {
        if (MapUtils.isEmpty(map)) {
            throw new APIRuntimeException(statusMsg, message);
        }
    }

    public static void notEmpty(Object[] array, String message) {
        if (ArrayUtils.isEmpty(array)) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
        }
    }
    public static void notEmpty(Object[] array, Integer status, String message) {
        if (ArrayUtils.isEmpty(array)) {
            throw new APIRuntimeException(status, message);
        }
    }
    public static void notEmpty(Object[] array, IBaseStatusMsg statusMsg) {
        if (ArrayUtils.isEmpty(array)) {
            throw new APIRuntimeException(statusMsg);
        }
    }
    public static void notEmpty(Object[] array, IBaseStatusMsg statusMsg, String message) {
        if (ArrayUtils.isEmpty(array)) {
            throw new APIRuntimeException(statusMsg, message);
        }
    }
    public static <T> void isEmpty(Collection<T> collection, String message) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
        }
    }
    public static <T> void isEmpty(Collection<T> collection, Integer status, String message) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new APIRuntimeException(status, message);
        }
    }
    public static <T> void isEmpty(Collection<T> collection, IBaseStatusMsg statusMsg) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new APIRuntimeException(statusMsg);
        }
    }
    public static <T> void isEmpty(Collection<T> collection, IBaseStatusMsg statusMsg, String message) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new APIRuntimeException(statusMsg, message);
        }
    }

    public static <K, V> void isEmpty(Map<K, V> map, String message) {
        if (MapUtils.isNotEmpty(map)) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
        }
    }
    public static <K, V> void isEmpty(Map<K, V> map, Integer status, String message) {
        if (MapUtils.isNotEmpty(map)) {
            throw new APIRuntimeException(status, message);
        }
    }
    public static <K, V> void isEmpty(Map<K, V> map, IBaseStatusMsg statusMsg) {
        if (MapUtils.isNotEmpty(map)) {
            throw new APIRuntimeException(statusMsg);
        }
    }
    public static <K, V> void isEmpty(Map<K, V> map, IBaseStatusMsg statusMsg, String message) {
        if (MapUtils.isNotEmpty(map)) {
            throw new APIRuntimeException(statusMsg, message);
        }
    }

    public static void isEmpty(Object[] array, String message) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
        }
    }
    public static void isEmpty(Object[] array, Integer status, String message) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new APIRuntimeException(status, message);
        }
    }
    public static void isEmpty(Object[] array, IBaseStatusMsg statusMsg) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new APIRuntimeException(statusMsg);
        }
    }
    public static void isEmpty(Object[] array, IBaseStatusMsg statusMsg, String message) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new APIRuntimeException(statusMsg, message);
        }
    }
    /**<==empty判定 end==>**/
    /**<==blank判定 begin==>**/
    public static void notBlank(CharSequence cs, String message) {
        if (StringUtils.isBlank(cs)) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
        }
    }
    public static void notBlank(CharSequence cs, Integer status, String message) {
        if (StringUtils.isBlank(cs)) {
            throw new APIRuntimeException(status, message);
        }
    }
    public static void notBlank(CharSequence cs, IBaseStatusMsg statusMsg) {
        if (StringUtils.isBlank(cs)) {
            throw new APIRuntimeException(statusMsg);
        }
    }
    public static void notBlank(CharSequence cs, IBaseStatusMsg statusMsg, String message) {
        if (StringUtils.isBlank(cs)) {
            throw new APIRuntimeException(statusMsg, message);
        }
    }
    /**<==blank判定 end==>**/
    /**<==响应结果判定 begin==>**/
    public static void isSuccess(IBaseResponse baseResponse) {
        notNull(baseResponse, IBaseStatusMsg.APIEnum.FAILED);
        if (!success(baseResponse)) {
            throw new APIRuntimeException(baseResponse.getStatus(), baseResponse.getMessage());
        }
    }

    public static void isSuccess(IBaseResponse baseResponse, String message) {
        notNull(baseResponse, IBaseStatusMsg.APIEnum.FAILED, message);
        if (!success(baseResponse)) {
            throw new APIRuntimeException(baseResponse.getStatus(), message);
        }
    }

    public static void isSuccess(IBaseResponse baseResponse, IBaseStatusMsg statusMsg) {
        notNull(baseResponse, statusMsg);
        if (!success(baseResponse)) {
            throw new APIRuntimeException(statusMsg);
        }
    }
    public static void isSuccess(IBaseResponse baseResponse, IBaseStatusMsg statusMsg, String message) {
        notNull(baseResponse, statusMsg, message);
        if (!success(baseResponse)) {
            throw new APIRuntimeException(statusMsg, message);
        }
    }

    private static boolean success(IBaseResponse baseResponse) {
        return IBaseStatusMsg.APIEnum.SUCCESS.getCode().equals(baseResponse.getStatus());
    }
    /**<==响应结果判定 end==>**/
    /**<==equal判定 begin==>**/
    public static void equal(Integer v1, Integer v2, String message) {
        if (v1 == null) {
            if (v2 != null) {
                throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
            }
        } else if (!v1.equals(v2)) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
        }
    }
    public static void equal(Integer v1, Integer v2, IBaseStatusMsg statusMsg) {
        if (v1 == null) {
            if (v2 != null) {
                throw new APIRuntimeException(statusMsg);
            }
        } else if (!v1.equals(v2)) {
            throw new APIRuntimeException(statusMsg);
        }
    }
    public static void equal(Integer v1, Integer v2, IBaseStatusMsg statusMsg, String message) {
        if (v1 == null) {
            if (v2 != null) {
                throw new APIRuntimeException(statusMsg, message);
            }
        } else if (!v1.equals(v2)) {
            throw new APIRuntimeException(statusMsg, message);
        }
    }

    public static void equal(Object v1, Object v2, String message) {
        if (v1 == null) {
            if (v2 != null) {
                throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
            }
        } else if (!v1.equals(v2)) {
            throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
        }
    }
    public static void equal(Object v1, Object v2, IBaseStatusMsg statusMsg) {
        if (v1 == null) {
            if (v2 != null) {
                throw new APIRuntimeException(statusMsg);
            }
        } else if (!v1.equals(v2)) {
            throw new APIRuntimeException(statusMsg);
        }
    }
    public static void equal(Object v1, Object v2, IBaseStatusMsg statusMsg, String message) {
        if (v1 == null) {
            if (v2 != null) {
                throw new APIRuntimeException(statusMsg, message);
            }
        } else if (!v1.equals(v2)) {
            throw new APIRuntimeException(statusMsg, message);
        }
    }
    /**<==equal判定 end==>**/
    /**<==集合contain判定 begin==>**/
    public static <T> void notContains(Collection<T> collection, T e, String message) {
        if (!CollectionUtils.isEmpty(collection)) {
            if (collection.contains(e)) {
                throw new APIRuntimeException(IBaseStatusMsg.APIEnum.PARAM_ERROR, message);
            }
        }
    }
    public static <T> void notContains(Collection<T> collection, T e, Integer status, String message) {
        if (!CollectionUtils.isEmpty(collection)) {
            if (collection.contains(e)) {
                throw new APIRuntimeException(status, message);
            }
        }
    }
    public static <T> void notContains(Collection<T> collection, T e, IBaseStatusMsg statusMsg) {
        if (!CollectionUtils.isEmpty(collection)) {
            if (collection.contains(e)) {
                throw new APIRuntimeException(statusMsg);
            }
        }
    }
    public static <T> void notContains(Collection<T> collection, T e, IBaseStatusMsg statusMsg, String message) {
        if (!CollectionUtils.isEmpty(collection)) {
            if (collection.contains(e)) {
                throw new APIRuntimeException(statusMsg, message);
            }
        }
    }
    /**<==集合contain判定 end==>**/
}
