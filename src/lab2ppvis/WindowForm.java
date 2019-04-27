/*
 * Copyright (C) 2019 BSUIR
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package lab2ppvis;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * @author Artsiom Suruntovich
 */
public class WindowForm {



    public void WindowConfig(Shell shell, String WindowHeader) {
         GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.makeColumnsEqualWidth = true;
        shell.setText(WindowHeader);
        shell.setLayout(gridLayout);
    }

   public static void WindowOpen(Display display,Shell NameOfshell, int ShellWidth, int ShellHeight) {
        NameOfshell.setSize(ShellWidth, ShellHeight);
        //shell.pack();
        NameOfshell.open();
        while (!NameOfshell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    public static void Error(Shell shell, String ErrorNumber, String ErrorMessage) {
        MessageBox mb = new MessageBox(shell, SWT.ERROR | SWT.OK);
        mb.setText(ErrorNumber);
        mb.setMessage(ErrorMessage);
        mb.open();
    }
}
