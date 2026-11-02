package es.protobuilder.model;

import jakarta.persistence.*;
import java.io.Serializable;

// Tabla que almacena la información global del perfil del jugador extraída de Enka API (ZZZ o Endfield)
@Entity
@Table(name = "perfiles_jugador")
public class PerfilJugador implements Serializable {

    private static final long serialVersionUID = 1L;

    // El UID del jugador en el juego actúa como Clave Primaria
    @Id
    @Column(name = "uid")
    private String uid;

    // Nombre del juego al que pertenece este perfil ("ZZZ" o "ENDFIELD")
    @Column(name = "juego", nullable = false)
    private String juego;

    // Apodo/Nickname del jugador en el juego
    @Column(name = "nickname")
    private String nickname;

    // Nivel de Interventor / Nivel de Cuenta (Inter-Knot Level en ZZZ)
    @Column(name = "level")
    private Integer level;

    // Mensaje de firma o estado público del jugador
    @Column(name = "signature")
    private String signature;

    // Nivel de Mundo / Fase de Reputación
    @Column(name = "world_level")
    private Integer worldLevel;

    // ID de la tarjeta de presentación o banner activo en el perfil
    @Column(name = "name_card_id")
    private String nameCardId;

    // ID del icono/avatar seleccionado por el jugador
    @Column(name = "avatar_icon_id")
    private String avatarIconId;

    // Cantidad total de logros o trofeos completados
    @Column(name = "finish_achievement_num")
    private Integer finishAchievementNum;

    // Constructor vacío exigido por JPA
    public PerfilJugador() {}

    // Getters y Setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getJuego() {
        return juego;
    }

    public void setJuego(String juego) {
        this.juego = juego;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getWorldLevel() {
        return worldLevel;
    }

    public void setWorldLevel(Integer worldLevel) {
        this.worldLevel = worldLevel;
    }

    public String getNameCardId() {
        return nameCardId;
    }

    public void setNameCardId(String nameCardId) {
        this.nameCardId = nameCardId;
    }

    public String getAvatarIconId() {
        return avatarIconId;
    }

    public void setAvatarIconId(String avatarIconId) {
        this.avatarIconId = avatarIconId;
    }

    public Integer getFinishAchievementNum() {
        return finishAchievementNum;
    }

    public void setFinishAchievementNum(Integer finishAchievementNum) {
        this.finishAchievementNum = finishAchievementNum;
    }
}