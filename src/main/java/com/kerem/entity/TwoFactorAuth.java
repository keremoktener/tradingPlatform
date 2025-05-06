package com.kerem.entity;

import com.kerem.constant.enums.VerificationType;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@Table(name = "tblTwoFactorAuths")
public class TwoFactorAuth {
    @Builder.Default
    private Boolean isTwoFactorAuthEnabled = false;

    private VerificationType sendTo;
}
