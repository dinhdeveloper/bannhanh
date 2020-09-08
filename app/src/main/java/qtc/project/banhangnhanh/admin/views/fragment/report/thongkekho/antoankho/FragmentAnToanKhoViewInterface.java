package qtc.project.banhangnhanh.admin.views.fragment.report.thongkekho.antoankho;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.AnToanKhoModel;

public interface FragmentAnToanKhoViewInterface extends BaseViewInterface {

    void init(HomeActivity activity,FragmentAnToanKhoViewCallback callback);

    void sendDataToView(ArrayList<AnToanKhoModel> list);
}
