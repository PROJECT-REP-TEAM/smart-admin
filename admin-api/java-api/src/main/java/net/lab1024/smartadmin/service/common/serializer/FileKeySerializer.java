package net.lab1024.smartadmin.service.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.file.service.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/15 15:06
 */
public class FileKeySerializer extends JsonSerializer<String> {

    @Autowired
    private FileService fileService;


    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (StringUtils.isEmpty(value)) {
            jsonGenerator.writeString(value);
            return;
        }
        if(fileService == null){
            jsonGenerator.writeString(value);
            return;
        }
        ResponseDTO<String> responseDTO = fileService.getFileUrl(value);
        if(responseDTO.isSuccess()){
            jsonGenerator.writeString(responseDTO.getData());
            return;
        }
        jsonGenerator.writeString(value);
    }
}
