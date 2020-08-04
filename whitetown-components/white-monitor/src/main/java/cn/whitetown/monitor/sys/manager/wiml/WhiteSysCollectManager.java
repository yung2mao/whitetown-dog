package cn.whitetown.monitor.sys.manager.wiml;

import cn.whitetown.dogbase.common.util.WhiteFormatUtil;
import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.monitor.sys.manager.SysCollectManager;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import cn.whitetown.monitor.sys.modo.po.*;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.*;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 监控管理实现
 * @author taixian
 * @date 2020/07/31
 **/
public enum WhiteSysCollectManager implements SysCollectManager {
    /**
     * 系统监控
     */
    WHITE_SYS_MONITOR;

    private static final long CPU_SLEEP_TIME = 1000;

    private SystemInfo systemInfo;
    private HardwareAbstractionLayer hardware;
    private OperatingSystem operatingSystem;

    private Properties sysProperties = System.getProperties();

    private long[] cpuPreTicks;
    private WhiteNetInfo oldNetInfo = null;

    private WhiteMonitorParams whiteMonitorParams = new WhiteMonitorParams();
    private WhiteSysBaseInfo sysBaseInfo = new WhiteSysBaseInfo();
    private WhiteCpuInfo whiteCpuInfo = new WhiteCpuInfo();
    private WhiteMemInfo memInfo = new WhiteMemInfo();
    private WhiteJvmInfo jvmInfo = new WhiteJvmInfo();
    private List<WhiteFileInfo> fileInfos = new LinkedList<>();
    private WhiteNetInfo netInfo = new WhiteNetInfo();
    private WhiteNetSpeed netSpeed = new WhiteNetSpeed();

    private WhiteSysCollectManager() {
        this.systemInfo = new SystemInfo();
        this.hardware = systemInfo.getHardware();
        this.operatingSystem = systemInfo.getOperatingSystem();
        whiteMonitorParams.setSysBaseInfo(sysBaseInfo);
        whiteMonitorParams.setWhiteCpuInfo(whiteCpuInfo);
        whiteMonitorParams.setMemInfo(memInfo);
        whiteMonitorParams.setJvmInfo(jvmInfo);
        whiteMonitorParams.setFileInfos(fileInfos);
        whiteMonitorParams.setNetInfo(netInfo);
        whiteMonitorParams.setNetSpeed(netSpeed);
    }

    @Override
    public WhiteSysBaseInfo getSysBaseInfo() {
        sysBaseInfo.setServerId(MonConfConstants.SERVER_ID);
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        sysBaseInfo.setVendor(operatingSystem.getManufacturer());
        sysBaseInfo.setVersion(operatingSystem.getFamily() + " " + operatingSystem.getVersion());
        sysBaseInfo.setArch(sysProperties.getProperty("os.arch"));
        Sensors sensors = systemInfo.getHardware().getSensors();
        sysBaseInfo.setCpuTemperature(sensors.getCpuTemperature()+"");
        //windows平台可能被限制
        sysBaseInfo.setFanSpeed(sensors.getFanSpeeds()[0]);
        long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        sysBaseInfo.setStartTime(WhiteFormatUtil.millisToDate(startTime));
        sysBaseInfo.setWorkTime(WhiteFormatUtil.
                timeScopeFormat(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS,null));
        sysBaseInfo.setTimeStamp(System.currentTimeMillis());
        return sysBaseInfo;
    }

    @Override
    public WhiteCpuInfo getCpuInfo() {
        CentralProcessor processor = hardware.getProcessor();
        whiteCpuInfo.setName(processor.getName());
        whiteCpuInfo.setVendor(processor.getVendor());
        whiteCpuInfo.setModel(processor.getIdentifier());
        whiteCpuInfo.setTotalCores(processor.getLogicalProcessorCount());
        if(cpuPreTicks == null) {
            cpuPreTicks = processor.getSystemCpuLoadTicks();
            Util.sleep(CPU_SLEEP_TIME);
        }
        long[] ticks = processor.getSystemCpuLoadTicks();
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - cpuPreTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - cpuPreTicks[CentralProcessor.TickType.NICE.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - cpuPreTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - cpuPreTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - cpuPreTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - cpuPreTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - cpuPreTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - cpuPreTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + ioWait + irq + softIrq + steal;
        cpuPreTicks = ticks;
        whiteCpuInfo.setSysUse((int)(sys * 1.0 / totalCpu * 10000));
        whiteCpuInfo.setUserUse((int)(user * 1.0 / totalCpu * 10000));
        whiteCpuInfo.setWait((int)(ioWait * 1.0 / totalCpu * 10000));
        whiteCpuInfo.setIdle((int)(idle * 1.0 / totalCpu *10000));

        whiteCpuInfo.setTimeStamp(System.currentTimeMillis());
        return whiteCpuInfo;

    }

    @Override
    public WhiteMemInfo getMemoryInfo() {
        GlobalMemory memory = hardware.getMemory();
        memInfo.setTotalMem(memory.getTotal());
        memInfo.setFreeMem(memory.getAvailable());
        memInfo.setUsedMem(memInfo.getTotalMem() - memInfo.getFreeMem());
        memInfo.setUsedPercent((int)(memInfo.getUsedMem() * 10000.0 / memInfo.getTotalMem()));
        memInfo.setTimeStamp(System.currentTimeMillis());
        return memInfo;
    }

    @Override
    public WhiteJvmInfo getJvmInfo() {
        Runtime runtime = Runtime.getRuntime();
        jvmInfo.setJvmTotal(runtime.totalMemory());
        jvmInfo.setJvmFree(runtime.freeMemory());
        jvmInfo.setJvmMax(runtime.maxMemory());
        jvmInfo.setJvmUsed(jvmInfo.getJvmTotal() - jvmInfo.getJvmFree());
        jvmInfo.setJvmUsedPercent((int)(jvmInfo.getJvmUsed() * 10000.0 / jvmInfo.getJvmTotal()));
        jvmInfo.setJvmVersion(sysProperties.getProperty("java.version"));
        jvmInfo.setJdkPath(sysProperties.getProperty("java.home"));
        jvmInfo.setTimeStamp(System.currentTimeMillis());
        return jvmInfo;
    }

    @Override
    public List<WhiteFileInfo> getFileInfo() {
        fileInfos.clear();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        FileSystem fileSystem = operatingSystem.getFileSystem();
        OSFileStore[] fileStores = fileSystem.getFileStores();
        Arrays.stream(fileStores).forEach(fileStore -> {
            WhiteFileInfo whiteFileInfo = new WhiteFileInfo();
            whiteFileInfo.setName(fileStore.getName());
            whiteFileInfo.setMount(fileStore.getMount());
            whiteFileInfo.setDescription(fileStore.getDescription());
            whiteFileInfo.setFsType(fileStore.getType());
            whiteFileInfo.setFileTotal(fileStore.getTotalSpace());
            whiteFileInfo.setFileFree(fileStore.getUsableSpace());
            whiteFileInfo.setFileUsed(whiteFileInfo.getFileTotal() - whiteFileInfo.getFileFree());
            whiteFileInfo.setTimeStamp(System.currentTimeMillis());
            this.fileInfos.add(whiteFileInfo);
        });
        return fileInfos;
    }

    @Override
    public WhiteNetInfo getNetInfo() {
        NetworkParams networkParams = operatingSystem.getNetworkParams();
        netInfo.setPrimaryDns(networkParams.getDnsServers()[0]);
        netInfo.setHostname(networkParams.getHostName());
        NetworkIF[] networkIFs = hardware.getNetworkIFs();
        netInfo.setIp(networkIFs[0].getIPv4addr()[0]);
        netInfo.setMacAddr(networkIFs[0].getMacaddr());
        long recBytes = 0;
        long sentBytes = 0;
        long errorBytes = 0;
        for(NetworkIF networkIF : networkIFs) {
            recBytes += networkIF.getBytesRecv();
            sentBytes += networkIF.getBytesSent();
            errorBytes += (networkIF.getInErrors() + networkIF.getOutErrors());
        }
        netInfo.setRecBytes(recBytes);
        netInfo.setSentBytes(sentBytes);
        netInfo.setErrorBytes(errorBytes);
        netInfo.setTimeStamp(System.currentTimeMillis());
        if(this.oldNetInfo == null) {

            this.oldNetInfo = netInfo.cloneObj();
        }

        return netInfo;
    }

    @Override
    public WhiteNetSpeed getNetSpeed() {
        netSpeed.setSendSpeed(netInfo.getSentBytes() - oldNetInfo.getSentBytes());
        netSpeed.setReceiveSpeed(netInfo.getRecBytes() - oldNetInfo.getRecBytes());
        netSpeed.setErrorSpeed(netInfo.getErrorBytes() - oldNetInfo.getErrorBytes());
        netSpeed.setTimeStamp(System.currentTimeMillis());
        this.oldNetInfo = netInfo.cloneObj();
        return netSpeed;
    }

    @Override
    public WhiteMonitorParams createMonitorParams() {
        this.getSysBaseInfo();
        this.getCpuInfo();
        this.getMemoryInfo();
        this.getJvmInfo();
        this.getFileInfo();
        this.getNetInfo();
        this.getNetSpeed();
        return whiteMonitorParams;
    }
}
