package io.outofbox.cronbot.controller.util;

public class ControllerUtils {

    public static int handleSize(int size){
        return size == 0 || size > 100 ? 50: size;
    }

    public static int handlePage(int page){
        return page >= 1 ? page-1: page;
    }
}
