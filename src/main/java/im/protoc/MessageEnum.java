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
        PING ("ping", "心跳ping" ),
        PONG("pong","心跳pong"),
        HANDSHAKE("handshake","请求握手");
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
        DISCARD( "404", "消息丢失" ),
        OTHERLOGIN("403","用户在其他地方登陆");//消息type为system时使用
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


    public enum delimiters{
        ENTER("\r\n","回车换行"),
        DOUBLEXX("**&&","特殊字符");
        private String code;
        private String comment;
        delimiters( String code, String comment) {
            this.code = code;
            this.comment = comment;
        }
        public static boolean isCode ( final String code ) {
            for ( delimiters value : delimiters.values() ) {
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
