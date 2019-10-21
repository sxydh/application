package charserver.cn.net.bhe.manage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WalletMapper {

    @Select({ " <script> "
            //
            + " SELECT rt.* FROM ( "
            //
            + " SELECT u.phone \"phone\", w.id \"id\", w.num \"num\", w.credit \"credit\", w.balance \"balance\", w.createtime \"createtime\", w.updatetime \"updatetime\", wl.remark \"lastOpt\", ROW_NUMBER() OVER(PARTITION BY w.id ORDER BY wl.createtime DESC) rnum FROM CS_WALLET w "
            //
            + " INNER JOIN CS_USER u ON w.user_id = u.id "
            //
            + " LEFT JOIN CS_WALLET_LOG wl ON wl.wallet_id = w.id "
            //
            + " WHERE u.type = 2 "
            //
            + " <choose> "
            //
            + " <when test='type==1 and role==0'> AND ((u.type = 1 AND u.role IN (1, 2)) OR (u.type = 2)) </when> "
            //
            + " <when test='type==1 and role!=0'> AND u.type = 2 </when> "
            //
            + " <otherwise> FALSE </otherwise> "
            //
            + " </choose> "
            //
            + " <if test='phone!=null and phone!=\"\"'> AND u.phone LIKE CONCAT('%', #{phone}, '%') </if> "
            //
            + " <if test='num!=null and num!=\"\"'> AND w.num LIKE CONCAT('%', #{num}, '%') </if> "
            //
            + " ) rt WHERE rnum = 1 "
            //
            + " ORDER BY num, id "
            //
            + " LIMIT #{offset}, #{limit} </script> " })
    public List<Map<String, Object>> walletList(Map<String, Object> rq_w);

    @Select({ " <script> SELECT COUNT(0) FROM ( "
            //
            + " SELECT rt.* FROM ( "
            //
            + " SELECT u.phone \"phone\", w.id \"id\", w.num \"num\", w.credit \"credit\", w.balance \"balance\", w.createtime \"createtime\", w.updatetime \"updatetime\", wl.remark \"lastOpt\", ROW_NUMBER() OVER(PARTITION BY w.id ORDER BY wl.createtime DESC) rnum FROM CS_WALLET w "
            //
            + " INNER JOIN CS_USER u ON w.user_id = u.id "
            //
            + " LEFT JOIN CS_WALLET_LOG wl ON wl.wallet_id = w.id "
            //
            + " WHERE u.type = 2 "
            //
            + " <choose> "
            //
            + " <when test='type==1 and role==0'> AND ((u.type = 1 AND u.role IN (1, 2)) OR (u.type = 2)) </when> "
            //
            + " <when test='type==1 and role!=0'> AND u.type = 2 </when> "
            //
            + " <otherwise> FALSE </otherwise> "
            //
            + " </choose> "
            //
            + " <if test='phone!=null and phone!=\"\"'> AND u.phone LIKE CONCAT('%', #{phone}, '%') </if> "
            //
            + " <if test='num!=null and num!=\"\"'> AND w.num LIKE CONCAT('%', #{num}, '%') </if> "
            //
            + " ) rt WHERE rnum = 1 "
            //
            + " ) c </script> " })
    public int walletCount(Map<String, Object> rq_w);

    @Select({ " <script> "
            //
            + " SELECT w.id \"id\", w.balance \"balance\", credit \"credit\" FROM CS_WALLET w WHERE w.num = #{num} FOR UPDATE "
            //
            + " </script> " })
    public Map<String, Object> walletCheckoutByNumForUpdate(@Param("num") String num);

    @Insert({ " <script> "
            //
            + " INSERT INTO CS_ORDER (id, wallet_id, type, amount, status, createtime, updatetime, credit, balance, operator_id) "
            //
            + " VALUES (#{id}, #{walletId}, #{type}, #{value}, #{status}, NOW(), NOW(), #{credit}, #{balance}, #{userId}) "
            //
            + " </script> " })
    public int orderAdd(Map<String, Object> rq_w);

    @Update({ " <script> "
            //
            + " UPDATE CS_WALLET SET updatetime = NOW() "
            //
            + " <if test='credit!=null'>, credit = #{credit} </if> "
            //
            + " <if test='balance!=null'>, balance = #{balance} </if> "
            //
            + " WHERE id = #{id} "
            //
            + " </script> " })
    public int walletUpdate(Map<String, Object> rq_w);

    @Insert({ " <script> "
            //
            + " INSERT INTO CS_WALLET_LOG (wallet_id, operator_id, createtime, remark) "
            //
            + " VALUES (#{walletId}, #{userId}, NOW(), #{remark}) "
            //
            + " </script> " })
    public int walletLogAdd(Map<String, Object> rq_w);

    @Select({ " <script> "
            //
            + " SELECT u.id \"id\", u.role \"role\", u.type \"type\" FROM CS_USER u "
            //
            + " INNER JOIN CS_WALLET w ON w.num = #{num} AND w.user_id = u.id "
            //
            + " </script> " })
    public Map<String, Object> userCheckoutByWalletNum(@Param("num") String num);

    @Select({ " <script> "
            //
            + " SELECT u.phone \"operator\", wl.createtime \"createtime\", wl.remark \"remark\" FROM CS_WALLET_LOG wl "
            //
            + " INNER JOIN CS_USER u ON wl.operator_id = u.id "
            //
            + " INNER JOIN CS_WALLET w ON w.num = #{num} AND wl.wallet_id = w.id "
            //
            + " WHERE 1 = 1 "
            //
            + " <if test='startDate!=null and startDate!=\"\"'> AND DATE_FORMAT(wl.createtime, '%Y-%m-%d') &gt;= #{startDate} </if> "
            //
            + " <if test='endDate!=null and endDate!=\"\"'> AND DATE_FORMAT(wl.createtime, '%Y-%m-%d') &lt;= #{endDate} </if> "
            //
            + " <if test='remark!=null and remark!=\"\"'> AND wl.remark LIKE CONCAT('%', #{remark}, '%') </if> "
            //
            + " ORDER BY wl.createtime DESC "
            //
            + " LIMIT #{offset}, #{limit} </script> " })
    public List<Map<String, Object>> logList(Map<String, Object> rq_w);

    @Select({ " <script> SELECT COUNT(0) FROM ( "
            //
            + " SELECT u.phone \"operator\", wl.createtime \"createtime\", wl.remark \"remark\" FROM CS_WALLET_LOG wl "
            //
            + " INNER JOIN CS_USER u ON wl.operator_id = u.id "
            //
            + " INNER JOIN CS_WALLET w ON w.num = #{num} AND wl.wallet_id = w.id "
            //
            + " WHERE 1 = 1 "
            //
            + " <if test='startDate!=null and startDate!=\"\"'> AND DATE_FORMAT(wl.createtime, '%Y-%m-%d') &gt;= #{startDate} </if> "
            //
            + " <if test='endDate!=null and endDate!=\"\"'> AND DATE_FORMAT(wl.createtime, '%Y-%m-%d') &lt;= #{endDate} </if> "
            //
            + " <if test='remark!=null and remark!=\"\"'> AND wl.remark LIKE CONCAT('%', #{remark}, '%') </if> "
            //
            + " ) c </script> " })
    public int logCount(Map<String, Object> rq_w);

    @Select({ " <script> "
            //
            + " SELECT o.id \"id\", o.createtime \"createtime\", o.type \"type\", o.amount \"amount\", o.credit \"credit\", o.balance \"balance\", o.status \"status\", u.phone operator FROM CS_ORDER o "
            //
            + " INNER JOIN CS_WALLET w ON w.num = #{num} AND o.wallet_id = w.id "
            //
            + " INNER JOIN CS_USER u ON u.id = o.operator_id "
            //
            + " WHERE 1 = 1 "
            //
            + " <if test='startDate!=null and startDate!=\"\"'> AND DATE_FORMAT(o.updatetime, '%Y-%m-%d') &gt;= #{startDate} </if> "
            //
            + " <if test='endDate!=null and endDate!=\"\"'> AND DATE_FORMAT(o.updatetime, '%Y-%m-%d') &lt;= #{endDate} </if> "
            //
            + " <if test='type!=null'> AND o.type = #{type} </if> "
            //
            + " ORDER BY o.createtime DESC, o.id "
            //
            + " LIMIT #{offset}, #{limit} </script> " })
    public List<Map<String, Object>> orderList(Map<String, Object> rq_w);

    @Select({ " <script> SELECT COUNT(0) FROM ( "
            //
            + " SELECT o.id \"id\", o.createtime \"createtime\", o.type \"type\", o.amount \"amount\", o.credit \"credit\", o.balance \"balance\", o.status \"status\", u.phone operator FROM CS_ORDER o "
            //
            + " INNER JOIN CS_WALLET w ON w.num = #{num} AND o.wallet_id = w.id "
            //
            + " INNER JOIN CS_USER u ON u.id = o.operator_id "
            //
            + " WHERE 1 = 1 "
            //
            + " <if test='startDate!=null and startDate!=\"\"'> AND DATE_FORMAT(o.updatetime, '%Y-%m-%d') &gt;= #{startDate} </if> "
            //
            + " <if test='endDate!=null and endDate!=\"\"'> AND DATE_FORMAT(o.updatetime, '%Y-%m-%d') &lt;= #{endDate} </if> "
            //
            + " <if test='type!=null'> AND o.type = #{type} </if> "
            //
            + " ) c </script> " })
    public int orderCount(Map<String, Object> rq_w);

}
