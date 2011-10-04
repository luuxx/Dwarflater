package me.luuxx.Dwarflater;

import org.json.simple.JSONValue;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Property {
    private Logger log;
    protected Dwarflater plugin;
    private LinkedHashMap<String, Object> properties = new LinkedHashMap<String, Object>();
    private String filename;
    private String pName;
    
    public Property(String filename, Dwarflater plugin) {
        this.plugin = plugin;
        this.pName = "Dwarflater";
        this.log = plugin.logger;
        this.filename = filename;
        File file = new File(filename);
        
        if (file.exists()) load();
    }
    
    // Load data from file into ordered HashMap
    public void load() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filename),"UTF-8"));
            String line;
            int cc = 0; // # of comments
            int lc = 0; // # of lines
            int delim;
            
            // While there are lines to read (auto-breaks)
            while ((line = br.readLine()) != null) {
                // Is a comment, store it
                if (line.charAt(0) == '#' && lc != 0) {
                    properties.put("#"+cc, line.substring(line.indexOf(' ')+1).trim());
                    cc++;
                    continue;
                }
                // Isn't a comment, store the key and value
                while ((delim = line.indexOf('=')) != -1) {
                    String key = line.substring(0, delim).trim();
                    String val = line.substring(delim+1).trim();
                    properties.put(key, val);
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            log.log(Level.SEVERE, '['+pName+"]: Couldn't find file "+filename, ex);
        } catch (IOException ex) {
            log.log(Level.SEVERE, '['+pName+"]: Unable to save "+filename, ex);
        } finally {
            // Close the reader
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                log.log(Level.SEVERE, '['+pName+"]: Unable to save "+filename, ex);
            }
        }
    }
    
    // Save data from LinkedHashMap to file
    public void save() {
        BufferedWriter bw = null;
        try {
            // Construct the BufferedWriter object
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"UTF-8"));
            bw.write("# "+pName+" Properties File");
            bw.newLine();
            
            // Save all the properties one at a time, only if there's data to write
            if (properties.size() > 0) {
                // Grab all the entries and create an iterator to run through them all
                Set<?> set = properties.entrySet();
                Iterator<?> i = set.iterator();
                
                // While there's data to iterate through..
                while (i.hasNext()) {
                    // Map the entry and save the key and value as variables
                    Map.Entry<?, ?> me = (Map.Entry<?, ?>)i.next();
                    String key = (String)me.getKey();
                    String val = me.getValue().toString();
                    
                    // If it starts with "#", it's a comment so write it as such
                    if (key.charAt(0) == '#') {
                        // Writing a comment to the file
                        bw.write("# "+val);
                        bw.newLine();
                    } else {
                        // Otherwise write the key and value pair as key=value
                        bw.write(key+'='+val);
                        bw.newLine();
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            log.log(Level.SEVERE, '['+pName+"]: Couldn't find file "+filename, ex);
            return;
        } catch (IOException ex) {
            log.log(Level.SEVERE, '['+pName+"]: Unable to save "+filename, ex);
            return;
        } finally {
            // Close the BufferedWriter
            try {
                if (bw != null) bw.close();
            } catch (IOException ex) {
                log.log(Level.SEVERE, '['+pName+"]: Unable to save "+filename, ex);
            }
        }
    }
    
    // Turn the Property into a JSON string
    public String toJSON() {
        return JSONValue.toJSONString(properties);
    }
    
    // Rebuild the current properties file using data from newMap
    public void rebuild(LinkedHashMap<String, Object> newMap) {
        properties.clear();
        properties.putAll(newMap);
        save();
    }
    
    // Function to check if current properties file matches a referenced one by validating every key
    public boolean match(LinkedHashMap<String, Object> prop) {
        return (properties.keySet().containsAll(prop.keySet())) ? true : false;
    }
    
    // Check if the key exists or not
    public boolean keyExists(String key) {
        return (properties.containsKey(key)) ? true : false;
    }
    
    // Return all the keys that exist
    public Set<String> getKeys() {
        return properties.keySet();
    }
    
    // Return all the stored values
    public Collection<Object> getValues() {
        return properties.values();
    }
    
    // Check if the key no value
    public boolean isEmpty(String key) {
        return (properties.get(key).toString().length() == 0) ? true : false;
    }
    
    // Increment the key by 1
    public void inc(String key) {
        BigDecimal v = new BigDecimal(properties.get(key).toString()).add(new BigDecimal("1"));
        properties.put(key, v.toString());
    }
    
    // Add given number to given key
    public void add(String key, Number n) {
        BigDecimal v = new BigDecimal(properties.get(key).toString()).add(new BigDecimal(""+n.toString()));
        properties.put(key, v.toString());
    }
    
    // Subtract given number from given key
    public void sub(String key, Number n) {
        BigDecimal v = new BigDecimal(properties.get(key).toString()).add(new BigDecimal(""+n.toString()));
        properties.put(key, v.toString());
    }
    
    // Remove key from map
    public boolean remove(String key) {
        return (properties.remove(key) != null) ? true : false;
    }
    
    // Set property value as a String
    public void setString(String key, String value) {
        properties.put(key, value);
    }
    
    // Set property value as a Number
    public void setNumber(String key, Number value) {
        properties.put(key, String.valueOf(value));
    }
    
    // Set property value as a Boolean
    public void setBool(String key, boolean value) {
        properties.put(key, String.valueOf(value));
    }
    
    // Get property value as a string
    public String getString(String key) {
        return (properties.containsKey(key)) ? properties.get(key).toString() : "";
    }
    
    // Get property value as a byte
    public byte getByte(String key) {
        return (properties.containsKey(key)) ? Byte.parseByte(properties.get(key).toString()) : 0;
    }
    
    // Get property value as a short
    public short getShort(String key) {
        return (properties.containsKey(key)) ? Short.parseShort(properties.get(key).toString()) : 0;
    }
    
    // Get property value as an int
    public int getInt(String key) {
        return (properties.containsKey(key)) ? Integer.parseInt(properties.get(key).toString()) : 0;
    }
    
    // Get property value as a double
    public double getDouble(String key) {
        return (properties.containsKey(key)) ? Double.parseDouble(properties.get(key).toString()) : 0.0D;
    }
    
    // Get property value as a long
    public long getLong(String key) {
        return (properties.containsKey(key)) ? Long.parseLong(properties.get(key).toString()) : 0L;
    }
    
    // Get property value as a float
    public float getFloat(String key) {
        return (properties.containsKey(key)) ? Float.parseFloat(properties.get(key).toString()) : 0F;
    }
    
    // Get property value as a boolean
    public boolean getBool(String key) {
        return (properties.containsKey(key)) ? Boolean.parseBoolean(properties.get(key).toString()) : false;
    }
}