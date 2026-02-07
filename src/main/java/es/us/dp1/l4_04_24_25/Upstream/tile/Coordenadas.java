package es.us.dp1.l4_04_24_25.Upstream.tile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Coordenadas {
    
    private int x;
    private int y;

    public Coordenadas(int x, int y) {

        this.x = x;

        this.y = y;

    }

}


