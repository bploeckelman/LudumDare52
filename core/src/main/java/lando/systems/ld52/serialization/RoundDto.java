package lando.systems.ld52.serialization;

public class RoundDto {
    public QuotaDto heaven;
    public QuotaDto hell;
    public TileDto[] tileDtos;

    public RoundDto(QuotaDto heaven, QuotaDto hell, TileDto... tileDtos) {
        this.heaven = heaven;
        this.hell = hell;
        this.tileDtos = tileDtos;
    }
}
