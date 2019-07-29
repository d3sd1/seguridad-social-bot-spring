package Workout.Selenium;

import Workout.Config.SSUrls;
import Workout.ORM.Model.AnulacionAltaPrevia;
import Workout.ORM.Model.Operation;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;

public class AnulacionAltaPreviaBot extends BaseBot {
    private AnulacionAltaPrevia op;

    public AnulacionAltaPreviaBot(Operation op, HashMap<String, Object> config) {
        super(op, config);
        this.op = (AnulacionAltaPrevia) op;
        this.logger.info("Processing operation " + this.op.getId());
        if (this.configure()) {
            this.manageOperation();
            this.destroy();
        }
    }

    protected void initialNavigate() {
        this.navigate(SSUrls.ANULACION_ALTA_CONSOLIDADA);
    }

    protected boolean firstForm() {
        /*
         * Rellenar número de afiliación
         * Primer campo, dos dígitos INT
         * Segundo campo, diez dígitos INT
         */
        this.getDriver().findElement(By.name("txt_SDFTESORNAF")).sendKeys(this.op.getNaf().substring(0, 2));
        this.getDriver().findElement(By.name("txt_SDFNUMNAF")).sendKeys(this.op.getNaf().substring(2, 10));

        /*
         * Rellenar régimen
         * Un sólo campo de 4 dígitos INT
         */
        this.getDriver().findElement(By.name("txt_SDFREGCC_ayuda")).sendKeys(this.op.getCca().getReg());

        /*
         * Rellenar cuenta de cotización
         * Primer campo, dos dígitos INT
         * Segundo campo, nueve dígitos INT
         */
        this.getDriver().findElement(By.name("txt_SDFTESCC")).sendKeys(this.op.getCca().getCcc().substring(0, 2));
        this.getDriver().findElement(By.name("txt_SDFNUMCC")).sendKeys(this.op.getCca().getCcc().substring(2, 11));

        /*
         *  Seleccionar tipo "anulación alta".
         */
        Select selectType = new Select(this.getDriver().findElement(By.name("cbo_ListaAltasBajas001")));
        selectType.selectByVisibleText("Alta");

        /*
         * Rellenar fecha real de la baja.
         * Tres campos (día, mes, año)
         */
        this.getDriver().findElement(By.name("txt_SDFDIAB")).sendKeys(String.valueOf(this.op.getFra().getDayOfMonth()));
        this.getDriver().findElement(By.name("txt_SDFMESB")).sendKeys(String.valueOf(this.op.getFra().getMonthValue()));
        this.getDriver().findElement(By.name("txt_SDFAOB")).sendKeys(String.valueOf(this.op.getFra().getYear()));

        /*
         *  Seleccionar tipo "anulación alta".
         */
        Select selectElimination = new Select(this.getDriver().findElement(By.id("ListaAltasBajas")));
        selectElimination.selectByVisibleText("Eliminación");


        /*
         * Clickar en el botón de enviar
         * Aquí concluye la primera parte del formulario
         */
        if (this.waitFormSubmit(By.name("btn_Sub2207101004"))) {
            return this.secondForm();
        }
        return false;
    }


    private boolean secondForm() {
        return this.waitFormSubmit(By.name("btn_Sub2207101004"));
    }

}
