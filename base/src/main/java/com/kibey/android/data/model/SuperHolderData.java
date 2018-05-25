package com.kibey.android.data.model;

import android.text.TextUtils;

import com.kibey.android.utils.APPConfig;
import com.kibey.android.utils.JsonUtils;
import com.kibey.android.utils.ListUtils;
import com.kibey.android.utils.StringUtils;
import com.kibey.manager.PluginManager;

import java.util.List;

public class SuperHolderData {
    private List<String> classes;
    private List<HolderInfo> holder_info;
    private List<DataInfo> data_info;

    public List<HolderInfo> getHolder_info() {
        if (null != holder_info) {

            for (HolderInfo holder : holder_info) {
                int index = StringUtils.parseInt(holder.holder, -1);
                if (index >= 0 && index < ListUtils.sizeOf(classes)) {
                    holder.setHolder(classes.get(index));
                }

                index = StringUtils.parseInt(holder.model, -1);
                if (index >= 0 && index < ListUtils.sizeOf(classes)) {
                    holder.setModel(classes.get(index));
                }
            }
        }
        return holder_info;
    }

    public void setHolder_info(List<HolderInfo> holder_info) {
        this.holder_info = holder_info;
    }

    public List<DataInfo> getData_info() {
        for (DataInfo data : data_info) {
            int index = StringUtils.parseInt(data.model, -1);
            if (index >= 0 && index < ListUtils.sizeOf(classes)) {
                data.setModel(classes.get(index));
            }
        }
        return data_info;
    }

    public void setData_info(List<DataInfo> data_info) {
        this.data_info = data_info;
    }

    public static class HolderInfo {
        /**
         * model :
         * holder :
         * plugin :
         */

        private String model;
        private String holder;
        private String plugin;

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getHolder() {
            return holder;
        }

        public void setHolder(String holder) {
            this.holder = holder;
        }

        public String getPlugin() {
            return plugin;
        }

        public void setPlugin(String plugin) {
            this.plugin = plugin;
        }
    }

    public static class DataInfo {
        /**
         * model :
         * data :
         */

        private String model;
        private Object data;
        private String plugin;

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public <T> T modelInstance() {
            String jsonModel = this.data.toString();
            Class modelClass = getClassByName(plugin, model);
            return JsonUtils.objectFromJson(jsonModel, (Class<T>) modelClass);
        }
    }

    public static Class getClassByName(String plugin, String className) {
        try {
            if (!TextUtils.isEmpty(plugin) && !plugin.equals(APPConfig.getPackageName())) {
                return PluginManager.getClassId(plugin, className);
            }
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
