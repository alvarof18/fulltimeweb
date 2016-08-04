  package com.casapazmino.fulltime.reportes;

  
  
  import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
  
  /**
   * Clase utilitaria, generada para el manejo de archivos, ademas incluye
   * una funcionalidad de compresion y descompersion en formato ZIP.
   */
  public class FileHandler 
  {
    /**
     * Tama�o por decto usado para el manejo del buffering.
     */
    private final int BUFFER_SIZE=8192;
    /**
     * Extencion ZIP.
     */
    public static final String ZIP_EXTENCION=".zip";
    /**
     * Extencion XML.
     */
    public static final String XML_EXTENCION=".XML";      
    /**
     * Arreglo de bytes en el cual se pone temporalmente la informaci�n de
     * los archivos durente el proceso de buffering.
     */
    private byte buffer[];
    /**
     * Objeto que tiene la informacion del input stream a ser utilizado
     * en el proceso a sea de lectura o escritura.
     */
    private ByteArrayInputStream inputStream;
    /**
     * Registra el path del archivo que se desea leer o escribir.
     */
    private String path;
    /**
     * Registra el path relativo del archivo que se desea leer o escribir.
     */
    private String relativePath;
    /**
     * Nombre del archivo que se desa leer o crear.
     */
    private String file;
    /**
     * Nombre del archivo temporal en caso de usarce la utilidad de genera-
     * cion de archivos temporales.
     */
    private String tempFile;
    /**
     * Entrada usada para asignar la entra del archivo ZIP.
     */
    private String fileEntry;
    
    /**
     * Sufijo utilizado para crear la referencia File.
     */
    private String sufix;
    /**
     * Prefijo (extencion archivo) utilido para crear la referencia File.
     */
    private String prefix;
    
    /**
     * Constructor por defecto.
     */
    public FileHandler()
    {
    }
    
    /**
     * Metodo utilido para la lectura de archivos de tama�o peque�o y su<br>
     * conversion a bytes.<br>
     * Obtiene el archivo de disco en base a los atributos <strong>path</strong><br> 
     * y <strong>file</strong> los cuales deben estar previamente parame-<br>
     * trizados.
     */
    public void transformInputStreamToByteArray()
     throws Exception
    {
      BufferedInputStream  buff;
      try 
      {
        buff = new BufferedInputStream( new FileInputStream( this.path + this.file ) ) ;
        buffer = new byte[ buff.available() ];
        buff.read( buffer );
      } 
      catch ( FileNotFoundException ex) 
      {
        throw new Exception(  
        "FileHandler.transformInputStreamToByteArray:" + ex.getMessage() );
      }
      catch ( IOException ex) 
      {
        throw new Exception( "FileHandler.transformInputStreamToByteArray:" + 
                                 ex.getMessage());
      }
      
    }
    
    /**
     * Metodo utilido para la lectura de archivos de tama�o grande y su<br>
     * conversion a bytes.<br>
     * Obtiene el archivo de disco en base a los atributos <strong>path</strong><br> 
     * y <strong>file</strong> los cuales deben estar previamente parame-<br>
     * trizados.
     */
    public void transformInputStreamToByteArrayBig()
     throws Exception
    {
      ByteArrayOutputStream byteOut;
      BufferedInputStream  buff;
      try 
      {
        buff = new BufferedInputStream( new FileInputStream( this.path + this.file ) ) ;
        byteOut = new ByteArrayOutputStream (); 
        buffer = new byte[ this.BUFFER_SIZE ];
        int count = 0;
        while((count = buff.read(this.buffer, 0, BUFFER_SIZE)) != -1) 
        {
          byteOut.write(buffer, 0, count);
        }
        buffer = byteOut.toByteArray();
      } 
      catch ( FileNotFoundException ex) 
      {
        throw new Exception( "FileHandler.transformInputStreamToByteArrayBig:" + 
                                 ex.getMessage() );
      }
      catch ( IOException ex) 
      {
        throw new Exception( "FileHandler.transformInputStreamToByteArrayBig:" +
                                 ex.getMessage() );
      }
      
    }
    
    /**
     * Metodo utiliza el input stream del objeto y lo convierte en bytes.<br>
     */
    public void inputStreamToByteArray( InputStream inputStream )
     throws Exception
    {
      BufferedInputStream  buff;
      try 
      {
        buff = new BufferedInputStream( inputStream ) ;
        buffer = new byte[ buff.available() ];
        buff.read( buffer );
      } 
      catch ( FileNotFoundException ex) 
      {
        throw new Exception( "FileHandler.inputStreamToByteArray:" + ex.getMessage() );
      }
      catch ( IOException ex) 
      {
        throw new Exception(  
        "FileHandler.inputStreamToByteArray:" + ex.getMessage() );
      }
      
    }
    
    /**
     * Metodo utilizadao para convertir un inputstream de tama�o grande y su<br>
     * conversion a bytes.<br>
     */
    public void inputStreamToByteArrayBig( InputStream inputStream )
     throws Exception
    {
      ByteArrayOutputStream byteOut;
      BufferedInputStream  buff;
      try 
      {
        buff = new BufferedInputStream( inputStream ) ;
        byteOut = new ByteArrayOutputStream (); 
        buffer = new byte[ this.BUFFER_SIZE ];
        int count = 0;
        while((count = buff.read(this.buffer, 0, BUFFER_SIZE)) != -1) 
        {
          byteOut.write(buffer, 0, count);
        }
        buffer = byteOut.toByteArray();
      } 
      catch ( FileNotFoundException ex) 
      {
        throw new Exception(  
        "FileHandler.inputStreamToByteArrayBig:" + ex.getMessage() );
      }
      catch ( IOException ex) 
      {
        throw new Exception(  
        "FileHandler.inputStreamToByteArrayBig:" + ex.getMessage() );
      }
      
    }
    
    /**
     * Metodo que transforma el arreglo a inputstream.
     * @param buffer Archivo en formato byte[]
     * @return Archivo en formato inputstream
     */
    public static InputStream getInputStreamFroByteArray( byte[] buffer ){
    	if( buffer != null ){
    		return new ByteArrayInputStream( buffer );
    	}
    	return null;
    }
    
    /**
     * Metodo que registra la informaci�n en un archivo peque�os, dado el <br>
     * <strong>path</strong> y <strong>file</strong>.
     */
    public void writeFile(  )
     throws Exception
    {
      FileOutputStream os;
      FileDescriptor fd;
      try 
      {
          os = new FileOutputStream( path + file );
          fd = os.getFD();
          os.write( buffer );
          os.flush();
          fd.sync();  
      } 
      catch ( FileNotFoundException ex) 
      {
        throw new Exception( "Error al Generar el Archivo XML.");

      }
      catch ( IOException ex) 
      {
        throw new Exception( "Error al Generar el Archivo XML.");

      }
    }
    
    /**
     * Metodo que registra la informaci�n en un archivo peque�os, dado el <br>
     * <strong>path</strong> y <strong>file</strong>.
     */
    public void writeFileBig( )
      throws Exception
    {
      FileOutputStream os;
      FileDescriptor fd;
      BufferedInputStream bis;
      byte data[] = new byte[ BUFFER_SIZE ];
      int count;
      try 
      {
          os = new FileOutputStream( path + file );
          fd = os.getFD();
          bis = new BufferedInputStream( new ByteArrayInputStream( this.buffer ) );
          while ( ( count = bis.read( data, 0, BUFFER_SIZE ) ) != -1 ) 
          {
                  os.write( buffer,0,count );       
          }
          os.flush();
          fd.sync();  
      } 
      catch ( FileNotFoundException ex) 
      {
        throw new Exception( "FileHandler.writeFileBig:" +  ex.getMessage() );
      }
      catch ( IOException ex) 
      {
        throw new Exception( "FileHandler.writeFileBig:" + ex.getMessage() );
      }
    }
    
    
  
    /**
     * Metodo que lee un archivo zip y lo maneja en memoria.
     * Descomprime el archivo zip en memoria, luego saca uno a uno los
     * elementos de dicho archivo y los combierte en bytes y en inputStream
     * para si pordelo manejar.
     */
    public void readZipFile()
     throws Exception
    {
      int count;
      byte data[] = new byte[ BUFFER_SIZE ];
      ZipInputStream zis;
      ZipEntry entry;
      ByteArrayOutputStream byteOut;
      try 
      {
          zis = new ZipInputStream( new ByteArrayInputStream( this.buffer ) );
          while( ( entry = zis.getNextEntry() ) != null )
          {
            if( !entry.isDirectory() )
            {
              byteOut = new ByteArrayOutputStream (); 
              while( (count = zis.read( data, 0, BUFFER_SIZE ) ) != -1 )
              {
                byteOut.write( data, 0, count );
              }
              this.buffer = byteOut.toByteArray();
              this.inputStream = new ByteArrayInputStream( this.buffer );
              byteOut.flush();
              byteOut.close();
            }
          }
          zis.close();
        }
        catch ( FileNotFoundException ex) 
      {
        throw new Exception( "FileHandler.readZipFile:" + ex.getMessage() );
      }
      catch ( IOException ex) 
      {
        throw new Exception( "FileHandler.readZipFile:" + ex.getMessage() );
      }
      
    }
    
    
    /**
     * Metodo que lee un archivo zip y descomprime su contenido al disco.
     */
    public void readZipFileSave(  )
      throws Exception
    {
      BufferedOutputStream dest = null;
      int count;
      byte data[] = new byte[ BUFFER_SIZE ];
      ZipInputStream zis;
      ZipEntry entry;
      String destination; 
      FileOutputStream fos;
      try 
      {
          zis = new ZipInputStream( new ByteArrayInputStream( this.buffer ) );
          while( ( entry = zis.getNextEntry() ) != null )
          {
            if( !entry.isDirectory() )
            {
              destination = path + File.separator + entry.getName();
              fos = new FileOutputStream( destination );
              dest = new BufferedOutputStream( fos, BUFFER_SIZE );
              while( (count = zis.read( data, 0, BUFFER_SIZE ) ) != -1 )
              {
                dest.write( data, 0, count );
              }
              dest.flush();
              dest.close();
            }
          }
          zis.close();
      }
      catch ( FileNotFoundException ex) 
      {
        throw new Exception( "FileHandler.readZipFileSave:" +ex.getMessage());
      }
      catch ( IOException ex) 
      {
        throw new Exception( "FileHandler.readZipFileSave:" +ex.getMessage() );
      }
    }
    
    
    /**
     * Metodo que realiza la creaci�n de un archivo ZIP, en base
     * a la infomaci�n del archivo que se encuentra en memoria. Luego saca la <br>
     * informacion del archivo que se encuentre dentro del zip y lo pone en <br>
     * el buffer de bytes para su tratamiento posterior. 
     */
    public void createZipFile()
      throws Exception
    {
      int count;
      byte data[] = new byte[ BUFFER_SIZE ];
      BufferedInputStream  bis;
      ZipEntry entry;
      ZipOutputStream out;
      ByteArrayOutputStream dest;
      try 
      {
          dest = new ByteArrayOutputStream( );//new FileOutputStream( temp );
          out = new ZipOutputStream( dest );
          bis = new BufferedInputStream( new ByteArrayInputStream( this.buffer ) );
          entry = new ZipEntry(  this.fileEntry );
          out.putNextEntry( entry );
          count = 0;
          while( count  != -1 )
          {
                    count = bis.read( data, 0, BUFFER_SIZE );
                  if( count  != -1 )
                          out.write( data, 0, count );
          }
          out.closeEntry();
          out.flush();
          out.close();
          this.buffer = dest.toByteArray();
          bis.close();
       }
       catch ( FileNotFoundException ex) 
       {
          ex.printStackTrace();
          throw new Exception( "FileHandler.createZipFile:" + ex.getMessage() );
       }
       catch ( IOException ex) 
       {
          ex.printStackTrace();
          throw new Exception( "FileHandler.createZipFile:" + ex.getMessage() );
       }
      
    }
    
    /**
     * metodo que elimina el archivo del sistema.
     * @throws Exception
     */
    public void deleteFile( ) throws Exception {
    	boolean success = (new File( this.path + this.file )).delete();
        if (!success) {
        	//throw new Exception( "ERROR: AL ELIMINAR EL ARCHIVO " + this.path + this.file );
        }
    }
  
    public void setBuffer(byte[] buffer)
    {
      this.buffer = buffer;
    }
  
  
    public byte[] getBuffer()
    {
      return buffer;
    }
  
  
    public void setInputStream(ByteArrayInputStream inputStream)
    {
      this.inputStream = inputStream;
    }
  
  
    public ByteArrayInputStream getInputStream()
    {
      return inputStream;
    }
  
  
    public void setPath(String path)
    {
      this.path = path;
    }
  
  
    public String getPath()
    {
      return path;
    }
  
  
    public void setFile(String file)
    {
      this.file = file;
    }
  
  
    public String getFile()
    {
      return file;
    }
  
    public void setTempFile(String tempFile)
    {
      this.tempFile = tempFile;
    }
  
  
    public String getTempFile()
    {
      return tempFile;
    }
  
    public void setSufix(String sufix)
    {
      this.sufix = sufix;
    }
  
  
    public String getSufix()
    {
      return sufix;
    }
  
  
    public void setPrefix(String prefix)
    {
      this.prefix = prefix;
    }
  
  
    public String getPrefix()
    {
      return prefix;
    }
    
    public void setRelativePath( String relativePath )
    {
      this.relativePath = relativePath;
    }
  
  
    public String getRelativePath()
    {
      return relativePath;
    }
    
    public void setFileEntry( String fileEntry )
    {
      this.fileEntry = fileEntry;
    }
  
  
    public String getFileEntry()
    {
      return fileEntry;
    }
  
  
    
  
  
      
  }