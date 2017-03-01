package im.protoc;

/**
 * Created by Misnearzhang on 2017/3/1.
 */
public abstract class MessageEnum {
    public enum type{
        user( "user", "用户消息" ),
        system( "system", "系统消息" ),
        response( "response", "响应消息" ),
        heartbeat( "heartbeat", "心跳消息" );
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

    public enum status{
        OK( "200", "消息接受成功" ),
        error( "500", "系统错误" ),
        discard( "404", "消息丢失" );
        private String code;
        private String comment;
        status( String code, String comment) {
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
}
