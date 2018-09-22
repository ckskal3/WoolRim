package org.woolrim.woolrim;

import java.io.Serializable;
import java.util.List;

/*
    현재 임시로 만들어 둔거임.
    서버의 데이터 형식에 맞게 수정해야됨.
 */

public class Data {
    List<Result> result;
    public class Result  { //serializable  -> 데이터 인텐트시 필요함
        String name;
        double lat,lng;
    }
}
