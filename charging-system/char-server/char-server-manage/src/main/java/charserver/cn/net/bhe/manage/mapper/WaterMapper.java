package charserver.cn.net.bhe.manage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

public interface WaterMapper {

    @Select({ " <script> "
            //
            + " SELECT rt.* FROM ( "
            //
            + " SELECT u.phone \"phone\", w.id \"id\", w.eqm_num \"eqmNum\", w.old_value \"oldValue\", w.new_value \"newValue\", w.createtime \"createtime\", w.updatetime \"updatetime\", wl.remark \"lastOpt\", ROW_NUMBER() OVER(PARTITION BY w.id ORDER BY wl.createtime DESC) rnum FROM CS_WATER w "
            //
            + " INNER JOIN CS_USER u ON w.user_id = u.id "
            //
            + " LEFT JOIN CS_WATER_LOG wl ON wl.water_id = w.id "
            //
            + " WHERE u.type = 2 "
            //
            + " <if test='phone!=null and phone!=\"\"'> AND u.phone LIKE CONCAT('%', #{phone}, '%') </if> "
            //
            + " <if test='eqmNum!=null and eqmNum!=\"\"'> AND w.eqm_num LIKE CONCAT('%', #{eqmNum}, '%') </if> "
            //
            + " ) rt WHERE rnum = 1 "
            //
            + " ORDER BY eqmNum, id "
            //
            + " LIMIT #{offset}, #{limit} </script> " })
    public List<Map<String, Object>> waterList(Map<String, Object> rq_w);

    @Select({ " <script> SELECT COUNT(0) FROM ( "
            //
            + " SELECT rt.* FROM ( "
            //
            + " SELECT u.phone \"phone\", w.id \"id\", w.eqm_num \"eqmNum\", w.old_value \"oldValue\", w.new_value \"newValue\", w.createtime \"createtime\", w.updatetime \"updatetime\", wl.remark \"lastOpt\", ROW_NUMBER() OVER(PARTITION BY w.id ORDER BY wl.createtime DESC) rnum FROM CS_WATER w "
            //
            + " INNER JOIN CS_USER u ON w.user_id = u.id "
            //
            + " LEFT JOIN CS_WATER_LOG wl ON wl.water_id = w.id "
            //
            + " WHERE u.type = 2 "
            //
            + " <if test='phone!=null and phone!=\"\"'> AND u.phone LIKE CONCAT('%', #{phone}, '%') </if> "
            //
            + " <if test='eqmNum!=null and eqmNum!=\"\"'> AND w.eqm_num LIKE CONCAT('%', #{eqmNum}, '%') </if> "
            //
            + " ) rt WHERE rnum = 1 "
            //
            + " ) c </script> " })
    public int waterCount(Map<String, Object> rq_w);

    @Select({ " <script> "
            //
            + " SELECT u.phone \"operator\", wl.createtime \"createtime\", wl.remark \"remark\" FROM CS_WATER_LOG wl "
            //
            + " INNER JOIN CS_USER u ON wl.operator_id = u.id "
            //
            + " INNER JOIN CS_WATER w ON w.eqm_num = #{eqmNum} AND wl.water_id = w.id "
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
            + " SELECT u.phone \"operator\", wl.createtime \"createtime\", wl.remark \"remark\" FROM CS_WATER_LOG wl "
            //
            + " INNER JOIN CS_USER u ON wl.operator_id = u.id "
            //
            + " INNER JOIN CS_WATER w ON w.eqm_num = #{eqmNum} AND wl.water_id = w.id "
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

}
