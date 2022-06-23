package polaris.core.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("specify_reply")
public class SpecifyReply {
    @TableField("key")
    private String key;
    @TableField("string")
    private String string;
    @TableField("image")
    private String image;
    @TableField("voice")
    private String voice;
    @TableField("random")
    private int random;
    @TableField("recall")
    private int recall;
    @TableField("flash_image")
    private String flash_image;
    @TableField("txt")
    private String txt;
    @TableField("rule")
    private String rule;
    @TableField("request_method")
    private int request_method;
    @TableField("json_key_path")
    private String json_key_path;
}
