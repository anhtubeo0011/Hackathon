package com.example.cms.dto.request;

import com.example.cms.dto.EmployeeDTO;
import com.example.cms.dto.TaskDTO;
import lombok.Data;
import java.util.List;

@Data
public class SmartTaskRequest {
    private List<TaskDTO> taskDTOList;
    private List<EmployeeDTO> employeeDTOList;
}
