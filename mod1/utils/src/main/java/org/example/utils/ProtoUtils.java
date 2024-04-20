package org.example.utils;

import com.google.protobuf.Timestamp;
import org.example.domain.Angajat;
import org.example.domain.Bilet;
import org.example.domain.Meci;
import org.example.domain.MeciL;
import org.example.proto.ProjectProto;

import java.lang.reflect.Executable;
import java.time.LocalDate;
import java.util.ArrayList;

public class ProtoUtils {
    public static ProjectProto.Request createLoginRequest(Angajat angajat){
        ProjectProto.Angajat ang = ProjectProto.Angajat.newBuilder()
                .setParola(angajat.getParola()).setUsername(angajat.getUsername()).build();
        ProjectProto.Request request = ProjectProto.Request.newBuilder().setAngajat(ang)
                .setType(ProjectProto.Request.Type.LOGIN).build();
        return request;
    }

    public static ProjectProto.Request createLogoutRequest(int idAngajat){
        ProjectProto.Request request = ProjectProto.Request.newBuilder().setType(ProjectProto.Request
                .Type.LOGOUT).setIdAngajat(idAngajat).build();
        return request;
    }

    public static ProjectProto.Request createGetMeciuriRequest(){
        ProjectProto.Request request = ProjectProto.Request.newBuilder().setType(ProjectProto.Request
                .Type.GET_MECIURI).build();
        return request;
    }

    public static ProjectProto.Request createGetMeciuriLibereRequest(){
        ProjectProto.Request request = ProjectProto.Request.newBuilder().setType(ProjectProto.Request
                .Type.GET_MECIURI_LIBERE).build();
        return request;
    }

    public static ProjectProto.Request createCumparaBiletRequest(Bilet bilet){
        Meci m = bilet.getMeci();
        LocalDate ld = m.getData();
        ProjectProto.MyDate data = ProjectProto.MyDate.newBuilder().setDay(ld.getDayOfMonth())
                .setMonth(ld.getMonthValue()).setYear(ld.getYear()).build();
        ProjectProto.Meci meci = ProjectProto.Meci.newBuilder().setId(m.getId()).setData(data)
                .setCapacitate(m.getCapacitate()).setNume(m.getNume()).setPretBilet(m.getPretBilet()).build();
        ProjectProto.Bilet bil = ProjectProto.Bilet.newBuilder().setMeci(meci)
                .setNrLocuri(bilet.getNrLocuri()).setNumeClient(bilet.getNumeClient()).build();
        ProjectProto.Request request = ProjectProto.Request.newBuilder().setType(ProjectProto.Request
                .Type.CUMPARA_BILET).setBilet(bil).build();
        return request;
    }

    public static ProjectProto.Request createLocuriDisponibileRequest(Meci m){
        LocalDate ld = m.getData();
        ProjectProto.MyDate data = ProjectProto.MyDate.newBuilder().setDay(ld.getDayOfMonth())
                .setMonth(ld.getMonthValue()).setYear(ld.getYear()).build();
        ProjectProto.Meci meci = ProjectProto.Meci.newBuilder().setId(m.getId()).setData(data)
                .setCapacitate(m.getCapacitate()).setNume(m.getNume()).setPretBilet(m.getPretBilet()).build();
        ProjectProto.Request request = ProjectProto.Request.newBuilder().setType(ProjectProto.Request
                .Type.LOCURI_DISPONIBILE).setMeci(meci).build();
        return request;
    }


    public static ProjectProto.Response createOkResponse(){
        ProjectProto.Response response = ProjectProto.Response.newBuilder()
                .setType(ProjectProto.Response.Type.OK).build();
        return response;
    }

    public static ProjectProto.Response createErrorResponse(String mesaj){
        ProjectProto.Response response = ProjectProto.Response.newBuilder()
                .setType(ProjectProto.Response.Type.ERROR).setMessage(mesaj).build();
        return response;
    }

    public static ProjectProto.Response createLoginResponse(int idAngajat){
        ProjectProto.Response response = ProjectProto.Response.newBuilder()
                .setType(ProjectProto.Response.Type.OK).setIdAngajat(idAngajat).build();
        return response;
    }

    public static ProjectProto.Response createNrLocuriDisponibileMeciResponse(int nrLocuriDosponibile){
        ProjectProto.Response response = ProjectProto.Response.newBuilder()
                .setType(ProjectProto.Response.Type.LOCURI_DISPONIBILE)
                .setNrLocuriDisponibile(nrLocuriDosponibile).build();
        return response;
    }

    public static ProjectProto.Response createCumparaBiletResponse(Bilet bilet){
        Meci m = bilet.getMeci();
        LocalDate ld = m.getData();
        ProjectProto.MyDate data = ProjectProto.MyDate.newBuilder().setDay(ld.getDayOfMonth())
                .setMonth(ld.getMonthValue()).setYear(ld.getYear()).build();
        ProjectProto.Meci meci = ProjectProto.Meci.newBuilder().setId(m.getId()).setData(data)
                .setCapacitate(m.getCapacitate()).setNume(m.getNume()).setPretBilet(m.getPretBilet()).build();
        ProjectProto.Bilet bil = ProjectProto.Bilet.newBuilder().setMeci(meci).setId(bilet.getId())
                .setNrLocuri(bilet.getNrLocuri()).setNumeClient(bilet.getNumeClient()).build();
        ProjectProto.Response response = ProjectProto.Response.newBuilder()
                .setType(ProjectProto.Response.Type.OK)
                .setBilet(bil).build();
        return response;
    }

    public static ProjectProto.Response createGetMeciuriResponse(ArrayList<MeciL> meciuri){

        ProjectProto.Response.Builder response = ProjectProto.Response.newBuilder()
                .setType(ProjectProto.Response.Type.OK);

        for (MeciL m: meciuri) {
            LocalDate ld = m.getData();
            ProjectProto.MyDate data = ProjectProto.MyDate.newBuilder().setDay(ld.getDayOfMonth())
                    .setMonth(ld.getMonthValue()).setYear(ld.getYear()).build();
            ProjectProto.MeciL meci = ProjectProto.MeciL.newBuilder().setId(m.getId()).setData(data)
                    .setNrLocuriDisponibile(m.getNrLocuriDisponibile()).setCapacitate(m.getCapacitate())
                    .setNume(m.getNume()).setPretBilet(m.getPretBilet()).build();
            response.addMeciuri(meci);
        }

        return response.build();
    }

    public static ProjectProto.Response createSchimbareMeciuriResponse(ArrayList<MeciL> meciuri){

        ProjectProto.Response.Builder response = ProjectProto.Response.newBuilder()
                .setType(ProjectProto.Response.Type.NEW_MECIURI_LIST);

        for (MeciL m: meciuri) {
            LocalDate ld = m.getData();
            ProjectProto.MyDate data = ProjectProto.MyDate.newBuilder().setDay(ld.getDayOfMonth())
                    .setMonth(ld.getMonthValue()).setYear(ld.getYear()).build();
            ProjectProto.MeciL meci = ProjectProto.MeciL.newBuilder().setId(m.getId()).setData(data)
                    .setNrLocuriDisponibile(m.getNrLocuriDisponibile()).setCapacitate(m.getCapacitate())
                    .setNume(m.getNume()).setPretBilet(m.getPretBilet()).build();
            response.addMeciuri(meci);
        }

        return response.build();
    }

    public static String getError(ProjectProto.Response response){
        String errorMessage = response.getMessage();
        return errorMessage;
    }

    public static LocalDate getDate(ProjectProto.MyDate myDate) {
        return LocalDate.of(myDate.getYear(), myDate.getMonth(), myDate.getDay());
    }

    public static Meci getMeci(ProjectProto.Meci meci) {
        Meci m = new Meci(meci.getNume(), meci.getPretBilet(), meci.getCapacitate(),
                getDate(meci.getData()));
        m.setId(meci.getId());
        return m;
    }

    public static MeciL getMeciL(ProjectProto.MeciL meci) {
        MeciL m = new MeciL(meci.getNume(), meci.getPretBilet(), meci.getCapacitate(),
                getDate(meci.getData()), meci.getNrLocuriDisponibile());
        m.setId(meci.getId());
        return m;
    }

    public static Angajat getAngajat(ProjectProto.Angajat angajat) {
        Angajat a = new Angajat(angajat.getParola(), angajat.getUsername());
        return a;
    }

    public static ArrayList<MeciL> getMeciuri(ProjectProto.Response response) {
        ArrayList<MeciL> meciuri = new ArrayList<>();
        for (ProjectProto.MeciL m: response.getMeciuriList()) {
            meciuri.add(getMeciL(m));
        }
        return meciuri;
    }

    public static Bilet getBilet(ProjectProto.Bilet bilet) {
        Meci meci = getMeci(bilet.getMeci());
        Bilet bilet_nou = new Bilet(meci, bilet.getNumeClient(), bilet.getNrLocuri());
        try {
            bilet_nou.setId(bilet.getId());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return bilet_nou;
    }

}
