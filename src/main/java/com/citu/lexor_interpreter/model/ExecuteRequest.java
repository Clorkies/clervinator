package com.citu.lexor_interpreter.model;

import java.util.List;

public record ExecuteRequest(String code, List<String> inputs) {}
