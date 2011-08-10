package cli.clamshell.impl;

import cli.clamshell.api.Command;
import cli.clamshell.api.Context;
import java.util.Map;

/**
 * This class implements the Help command.
 * <ul>
 * <li> Usage: help - displays description for all installed commands.
 * <li> Usage: help [command_name] displays command usage.
 * </ul>
 * @author vladimir.vivien
 */
public class HelpCmd implements Command{
    private static final String CMD_NAME = "help";
    private HelpCmdDescriptor descriptor = new HelpCmdDescriptor();
    
    private class HelpCmdDescriptor implements Command.Descriptor {
        public String getName() {
            return CMD_NAME;
        }

        public String getDescription() {
            return "Displays help information for available commands.";
        }

        public String getUsage() {
            return "Type 'help' or 'help [command_name]' ";
        }
    }
    
    @Override
    public Command.Descriptor getDescriptor() {
        return descriptor;
    }

    /**
     * Executes the Help command.
     * The help command expects format 'help [command_name]'.
     * If the optional command_name parameter is present, this class will 
     * display info about about the command.  If command_name is not present
     * the Help command displays a list of help for all installed command.
     * @param ctx
     * @return 
     */
    @Override
    public Object execute(Context ctx) {
        String[] args = (String[]) ctx.getValue(Context.KEY_COMMAND_LINE_ARGS);

        // if arg passed, display help for command matching arg.
        if(args != null && args.length > 0){
            printCommandHelp(ctx, args[0].trim());
        }else{
            printCommandHelp(ctx, this);
        }
        return null;
    }

    @Override
    public void plug(Context plug) {
        // no plugin action needed
    }
    
    private void printCommandHelp(Context ctx, String cmdName){
        Map<String, Command> commands = (Map<String,Command>) ctx.getValue(Context.KEY_COMMAND_MAP);
        if(commands != null){
            Command cmd = commands.get(cmdName);
            if(cmd != null){
                printCommandHelp(ctx, cmd);
            }else{
                ctx.getIoConsole().writeOutput(String.format("%nCommand [%s] is not found.%n%n", cmdName));
            }
        }
    }
    
    private void printCommandHelp(Context ctx, Command cmd){
        if(cmd != null){
            ctx.getIoConsole().writeOutput(String.format("%nCommand: %s - %s%n", descriptor.getName(), descriptor.getDescription()));
            ctx.getIoConsole().writeOutput(String.format("Usage: %s%n%n", descriptor.getUsage()));            
        }
    }
    
}
