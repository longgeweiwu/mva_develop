package com.itcc.mva.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class CallbackVo {

    private String id;
    private Properties properties;
    public static class Properties{

        @JsonProperty("_state_")
        private String state;

        public String getState() {
            return state;
        }

        public void setState_(String state) {
            this.state = state;
        }
    }

    private Body body;
    public class Body{

        private String aid;
        private List<Lattice> lattices = new ArrayList();
        public class Lattice{

            private int lid;
            private int spk;
            private int begin;
            private int end;
            private float sc;
            @JsonProperty("json_cn1best")
            private String jsonCn1best;
            @JsonProperty("onebest")
            private String oneBest;

            public Lattice() {
            }

            public int getLid() {
                return lid;
            }

            public void setLid(int lid) {
                this.lid = lid;
            }

            public int getSpk() {
                return spk;
            }

            public void setSpk(int spk) {
                this.spk = spk;
            }

            public int getBegin() {
                return begin;
            }

            public void setBegin(int begin) {
                this.begin = begin;
            }

            public int getEnd() {
                return end;
            }

            public void setEnd(int end) {
                this.end = end;
            }

            public float getSc() {
                return sc;
            }

            public void setSc(float sc) {
                this.sc = sc;
            }

            public String getJsonCn1best() {
                return jsonCn1best;
            }

            public void setJsonCn1best(String jsonCn1best) {
                this.jsonCn1best = jsonCn1best;
            }

            public String getOneBest() {
                return oneBest;
            }

            public void setOneBest(String oneBest) {
                this.oneBest = oneBest;
            }
        }
        private State state;
        public class State {
            private Integer code;
            private Boolean success;
            private Boolean ok;

            public Integer getCode() {
                return code;
            }

            public void setCode(Integer code) {
                this.code = code;
            }

            public Boolean getSuccess() {
                return success;
            }

            public void setSuccess(Boolean success) {
                this.success = success;
            }

            public Boolean getOk() {
                return ok;
            }

            public void setOk(Boolean ok) {
                this.ok = ok;
            }
        }

        public Body() {
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public List<Lattice> getLattices() {
            return lattices;
        }

        public void setLattices(List<Lattice> lattices) {
            this.lattices = lattices;
        }

        public State getState() {
            return state;
        }

        public void setState(State state) {
            this.state = state;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
