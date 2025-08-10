package com.example.cms.dto.request;

import com.example.cms.dto.EmployeeDTO;
import com.example.cms.dto.TaskDTO;
import com.example.cms.entity.Project;
import lombok.Data;
import java.util.List;

@Data
public class SmartTaskRequest {
    private Project project;
    private List<TaskDTO> taskDTOList;
    private List<EmployeeDTO> employeeDTOList;
}
