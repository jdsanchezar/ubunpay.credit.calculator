package com.ubuntec.security.session.management.domain.model.user.terms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TermsConditions {

    private String name;
    private boolean checked;
    private long date;
}
