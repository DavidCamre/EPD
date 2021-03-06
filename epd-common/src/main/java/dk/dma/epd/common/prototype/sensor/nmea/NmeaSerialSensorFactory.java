/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.epd.common.prototype.sensor.nmea;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.io.Resources;

/***
 * Inject required serial communication native library file into path and load native library
 * @author jtj-sfs
 *
 * TODO: Doing more clean "runtime libraries" injection is difficult but it should be possible. Could move this to the boostrap routines.
 */
public class NmeaSerialSensorFactory {
    
    private static final Path EPDNATIVEPATH = Paths.get(System.getProperty("java.io.tmpdir"),"/epdNative/").toAbsolutePath();

    public static NmeaSerialSensor create(String comPort, int portSpeed) {
        try {
            Files.createDirectories(EPDNATIVEPATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        unpackLibs();

        try {
            addToLibraryPath(EPDNATIVEPATH);
            System.loadLibrary("rxtxSerial");
        } catch (NoSuchFieldException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (UnsatisfiedLinkError e1) {
            System.out.println(System.getProperty("java.library.path"));
            e1.printStackTrace(); 
        }

        return new NmeaSerialSensor(comPort, portSpeed);
        
    }
    
    private static void addToLibraryPath(Path path)
            throws NoSuchFieldException, 
             IllegalAccessException {
        System.setProperty("java.library.path", path.toAbsolutePath().toString());
        Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
        fieldSysPath.setAccessible(true);
        fieldSysPath.set(null, null);
    }
    
    public static void unpackLibs() {
        String filename = "";
        String libDir = "";
        
        final String osArch =  System.getProperty("os.arch");
        final String osName = System.getProperty("os.name");
        
        if (osName.startsWith("Windows")) {
            filename = "rxtxSerial.dll";
            
            if (osArch.indexOf("64") != -1) {
                libDir = "Windows/mfz-rxtx-2.2-20081207-win-x64/";
            } else {
                libDir = "Windows/i368-mingw32/";
            }
            
            
        } else if (osName.equals("Linux")) {
            filename = "librxtxSerial.so";
            if (osArch.equals("amd64")) {
                libDir = "Linux/x86_64-unknown-linu-gnu/";
            } else {
                libDir = "Linux/i686-unknown-linux-gnu/";
            }
                    
        } else if (osName.startsWith("Mac")) {
            filename = "rxtxSerial.jnilib";
            libDir = "Mac_OS_X/";
            
        } else {
            return;
        }
                
        
        try {
            File dest = Paths.get(EPDNATIVEPATH.toAbsolutePath().toString(),filename).toAbsolutePath().toFile();      
            dest.createNewFile();
            
            FileOutputStream destOut = new FileOutputStream(dest);

            String resource = "/gnu/io/"+libDir+filename;
            System.out.println(resource);            
            
            
            Resources.copy(NmeaSerialSensorFactory.class.getResource(resource), destOut);
            
            destOut.close();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

       
    }

}
