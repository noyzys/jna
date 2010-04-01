package com.sun.jna.platform.win32;

import java.io.File;
import java.io.IOException;

import com.sun.jna.WString;
import com.sun.jna.platform.FileUtils;

public class W32FileUtils extends FileUtils {

    public boolean hasTrash() { 
    	return true; 
	}

    public void moveToTrash(File[] files) throws IOException {
        Shell32 shell = Shell32.INSTANCE;
        ShellAPI.SHFILEOPSTRUCT fileop = new ShellAPI.SHFILEOPSTRUCT();
        fileop.wFunc = ShellAPI.FO_DELETE;
        String[] paths = new String[files.length];
        for (int i=0;i < paths.length;i++) {
            paths[i] = files[i].getAbsolutePath();
        }
        fileop.pFrom = new WString(fileop.encodePaths(paths));
        fileop.fFlags = ShellAPI.FOF_ALLOWUNDO|ShellAPI.FOF_NOCONFIRMATION|ShellAPI.FOF_SILENT;
        int ret = shell.SHFileOperation(fileop);
        if (ret != 0) {
            throw new IOException("Move to trash failed: " + 
            		Kernel32Util.formatMessageFromLastErrorCode(ret));
        }
        if (fileop.fAnyOperationsAborted) {
            throw new IOException("Move to trash aborted");
        }
    }
}
