package org.pt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationDto {
    private Integer id;

    private Integer remaining;

    private String invitationCode;
}
