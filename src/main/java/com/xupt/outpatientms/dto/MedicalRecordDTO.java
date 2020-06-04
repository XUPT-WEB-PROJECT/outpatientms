package com.xupt.outpatientms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDTO {

    @NotNull(message = "medicalRecord不能为空")
    private String medicalRecord;

    @NotNull(message = "recordId不能为空")
    private String recordId;

    @NotNull(message = "userId不能为空")
    private String userId;

    @NotNull(message = "doctorId不能为空")
    private String doctorId;

}
