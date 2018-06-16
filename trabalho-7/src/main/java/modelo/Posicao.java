package modelo;

import java.io.Serializable;

public enum Posicao implements Serializable {
    GOLEIRO, ATACANTE, LATERAL_E, LATERAL_D, MEIO_CAMPO, ZAGUEIRO, CENTRO_AVANTE;
    
    @Override
    public String toString() {
        String[] strs = this.name().split("_");
        String retorno = "";
        for(int i = 0; i < strs.length; i++) {
            retorno += strs[i].charAt(0) + strs[i].substring(1).toLowerCase() 
                    + (i + 1 < strs.length ? " " : "");
        }
        return retorno;
    }
}