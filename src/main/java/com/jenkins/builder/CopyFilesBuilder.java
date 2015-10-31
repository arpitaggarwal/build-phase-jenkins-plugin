package com.jenkins.builder;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.FreeStyleProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.kohsuke.stapler.DataBoundConstructor;

public class CopyFilesBuilder extends Builder {

	private final String path;

	@DataBoundConstructor
	public CopyFilesBuilder(final String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	@Override
	public boolean perform(
			@SuppressWarnings("rawtypes") final AbstractBuild build,
			final Launcher launcher, final BuildListener listener)
			throws IOException {
		//logic to be executed by plugin
		final File file = new File(path);
		final File destination = new File(build.getWorkspace().getRemote());
		if (file.isDirectory()) {
			FileUtils.copyDirectory(file, destination);
		} else {
			FileUtils.copyFileToDirectory(file, destination);
		}
		//logger which prints on job 'Console Output'
		listener.getLogger().printf("Copying files from %s to %s", path,
				build.getWorkspace());

		return true;
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	@Extension
	public static class DescriptorImpl extends BuildStepDescriptor<Builder> {

		@Override
		public boolean isApplicable(
				@SuppressWarnings("rawtypes") Class<? extends AbstractProject> jobType) {
			return FreeStyleProject.class.isAssignableFrom(jobType);
		}

		@Override
		public String getDisplayName() {
			//String to be be displayed in 'Add Build Step' dropdown
			return "Copy Files To Workspace";
		}
	}
}