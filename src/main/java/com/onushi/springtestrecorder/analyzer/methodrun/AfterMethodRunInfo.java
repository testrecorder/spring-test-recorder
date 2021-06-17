package com.onushi.springtestrecorder.analyzer.methodrun;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AfterMethodRunInfo {
    protected Object result;
    protected Exception exception;
}
