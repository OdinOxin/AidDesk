package de.odinoxin.aidcloud.mapper;

import de.odinoxin.aidcloud.service.RefBoxEntity;
import de.odinoxin.aidcloud.service.RefBoxService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class RefBoxMapper {
    private static RefBoxService refBoxSvc;

    private static RefBoxService getSvc() {
        if (refBoxSvc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                refBoxSvc = new RefBoxService(new URL(Login.getServerUrl() + "/RefBox?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return refBoxSvc;
    }

    public static RefBoxListItem getItem(String name, int id) {
        if (RefBoxMapper.getSvc() != null) {
            RefBoxEntity item = RefBoxMapper.getSvc().getRefBoxPort().getItem(name, id);
            if (item != null) {
                return new RefBoxListItem(item.getId(), item.getText(), item.getSubText());
            }
        }
        return null;
    }

    public static List<RefBoxListItem> search(String name, String[] expr) {
        List<RefBoxListItem> res = new ArrayList<>();
        if (RefBoxMapper.getSvc() != null) {
            String[] search = expr;
            if (search == null)
                search = new String[0];
            List<RefBoxEntity> items = RefBoxMapper.getSvc().getRefBoxPort().search(name, Arrays.asList(search));
            if (items != null) {
                res.addAll(items.stream().map(item -> new RefBoxListItem(item.getId(), item.getText(), item.getSubText(), expr)).collect(Collectors.toList()));
            }
        }
        return res;
    }
}
