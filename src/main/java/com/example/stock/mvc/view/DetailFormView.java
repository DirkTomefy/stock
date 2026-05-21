package com.example.stock.mvc.view;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.example.stock.mvc.controller.DetailFormController;
import com.example.stock.mvc.model.Article;
import com.example.stock.mvc.model.DetailFormInput;
import com.example.stock.mvc.model.EtatStock;
import com.example.stock.mvc.service.MouvementDetailService;
import com.example.stock.mvc.view.module.form.DetailFormPanel;
import com.example.stock.mvc.view.module.table.DetailTablePanel;

public class DetailFormView extends JPanel {

    private final DetailFormPanel form;
    private final DetailTablePanel table;

    
    public DetailFormView() throws ClassNotFoundException, SQLException {
        setLayout(new BorderLayout(10, 10));

        this.form = new DetailFormPanel();
        this.table = new DetailTablePanel();
        initView();
    }

    public void initView(){
        DetailFormController controller = new DetailFormController(this);
        this.form.setValidateFormListener(controller);

        add(this.form, BorderLayout.NORTH);
        add(new JScrollPane(this.table), BorderLayout.CENTER);
    }

    public DetailFormView(EtatStock etatstock) throws Exception{
        setLayout(new BorderLayout(10, 10));
        this.form = new DetailFormPanel();
        this.table = new DetailTablePanel();
        initView();
        DetailFormInput dFormInput=(DetailFormInput) this.form.getObject();
        dFormInput.setDate(etatstock.getDateDernierMouvement());
        //TODO : prendre depuis le dao fa tsy maina
        dFormInput.setArticle(new Article(etatstock));

        this.table.setData(MouvementDetailService.findDetails(dFormInput));
        this.form.reloadInput();
    }

    public DetailFormPanel getForm() {
        return form;
    }

    public DetailTablePanel getTable() {
        return table;
    }
}
