package com.aquimercado.aquimercado.mercado;

/**
 * Created by thiago on 06/01/2016.
 */
public class MercadoItem {
    private String mName;
    private String mLatitude;
    private String mLongitude;
    private String mId;
    private String mThumbnail;
    private String mContagem;
    private String mDistancia;
    private String mSobreapp;
    private  String mPontos;


    public MercadoItem(String name, String imagem, String contagem, String id, String distancia, String sobre, String pontos){
        setmId(id);
        setmName(name);
        setmThumbnail(imagem);
        setmContagem(contagem);
        setmDistancia(distancia);
        setmSobreapp(sobre);
        setmPontos(pontos);
    }
    public MercadoItem(String name, String imagem, String contagem, String id, String distancia){
        setmId(id);
        setmName(name);
        setmThumbnail(imagem);
        setmContagem(contagem);
        setmDistancia(distancia);
    }
    public MercadoItem(String name, String imagem, String id, String distancia){
       setmId(id);
       setmName(name);
        setmThumbnail(imagem);
        setmDistancia(distancia);
    }


    public MercadoItem(){}

    //Get an Set
    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }
    public String getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getmContagem() {
        return mContagem;
    }

    public void setmContagem(String mContagem) {
        this.mContagem = mContagem;
    }

    public String getmDistancia() {
        return mDistancia;
    }

    public void setmDistancia(String mDistancia) {
        this.mDistancia = mDistancia;
    }

    public String getmSobreapp() {
        return mSobreapp;
    }

    public void setmSobreapp(String mSobreapp) {
        this.mSobreapp = mSobreapp;
    }
    public String getmPontos() {
        return mPontos;
    }

    public void setmPontos(String mPontos) {
        this.mPontos = mPontos;
    }

}
