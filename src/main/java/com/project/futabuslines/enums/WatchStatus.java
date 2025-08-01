package com.project.futabuslines.enums;

import lombok.Getter;

@Getter
public enum WatchStatus {
        SOLD_OUT("sold-out"),
        AVAILABLE("available");

        private final String value;

        WatchStatus(String value) {
            this.value = value;
        }

}