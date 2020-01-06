package com.itcc.mva.vo;

import java.util.List;
/**
 * @author whoami
 */
public class QuarkCallbackVo {

    private String aid;
    private String file_length;
    private List<Lattice> lattice;

    public static class Lattice {
        private String json_1best;

        public String getJson_1best() {
            return json_1best;
        }

        public void setJson_1best(String json_1best) {
            this.json_1best = json_1best;
        }
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getFile_length() {
        return file_length;
    }

    public void setFile_length(String file_length) {
        this.file_length = file_length;
    }

    public List<Lattice> getLattice() {
        return lattice;
    }

    public void setLattice(List<Lattice> lattice) {
        this.lattice = lattice;
    }
}
