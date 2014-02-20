
package larvaplug;

public interface _callable {

    //public void _call(String _info);
    //public void _call(String _infoSM, Long _info_pid, String _infoSI, String _prptyName);
    public void _call(String _infoSISM, Long _info_pid, String _infoSI);
    
    //public void _call_all_filtered(String _infoSM, Long _info_pid, String _infoSI, String _prptyName);
    public void _call_all_filtered(String _infoSISM, Long _info_pid, String _infoSI);
    



}
