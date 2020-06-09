package com.github.nmyphp.interfaces;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Collector {

    Logger logger = LoggerFactory.getLogger(Collector.class);

    List<String> listTable();
}
