package im.protoc;

/**
 * Created by Misnearzhang on 2017/3/1.
 */
public abstract class MessageEnum {

    /**
     * 消息类型
     */
    public enum type{
        USER( "user", "用户消息" ),
        SYSTEM( "system", "系统消息" ),
        RESPONSE( "response", "响应消息" ),
        HEARTBEAT( "heartbeat", "心跳消息" );
        private String code;
        private String comment;
        type( String code, String comment) {
            this.code = code;
            this.comment = comment;
        }
        public static boolean isCode ( final String code ) {
            for ( type value : type.values() ) {
                if ( value.getCode().equalsIgnoreCase( code ) ) {
                    return true;
                }
            }
            return false;
        }
        public static boolean isNotCode ( final String code ) {
            return ! isCode( code );
        }


        public String getComment () {
            return comment;
        }

        public String getCode () {
            return code;
        }
    }

    /**
     * 消息状态行
     */
    public enum status{
        REQ("100","请求消息"),
        OK( "200", "消息接受成功" ),
        ERROR( "500", "系统错误" ),
        DISCARD( "404", "消息丢失" );
        private String code;
        private String comment;
        status( String code, String comment) {
            this.code = code;
            this.comment = comment;
        }
        public static boolean isCode ( final String code ) {
            for ( status value : status.values() ) {
                if ( value.getCode().equalsIgnoreCase( code ) ) {
                    return true;
                }
            }
            return false;
        }
        public static boolean isNotCode ( final String code ) {
            return ! isCode( code );
        }


        public String getComment () {
            return comment;
        }

        public String getCode () {
            return code;
        }
    }

    /**
     * 系统推送消息类型
     */
    public enum systemType{
        LOGIN_ANOTHER("403","用户在其他地方登陆"),
        MESSAGE_SUCCESS( "200", "消息接受成功" );
        private String code;
        private String comment;
        systemType( String code, String comment) {
            this.code = code;
            this.comment = comment;
        }
        public static boolean isCode ( final String code ) {
            for ( systemType value : systemType.values() ) {
                if ( value.getCode().equalsIgnoreCase( code ) ) {
                    return true;
                }
            }
            return false;
        }
        public static boolean isNotCode ( final String code ) {
            return ! isCode( code );
        }


        public String getComment () {
            return comment;
        }

        public String getCode () {
            return code;
        }
    }
}
