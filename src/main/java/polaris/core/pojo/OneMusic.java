package polaris.core.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("music_list")
public class OneMusic {
    @TableField("mirai_code")
    private String mirai_code;
    @TableField("name")
    private String name;
    @TableField("key")
    private String key;
    @TableField("song_list")
    private String song_list;
}
