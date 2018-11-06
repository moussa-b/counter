package com.bdzapps.counterpp.folderlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bdzapps.counterpp.commons.CallBack;
import com.bdzapps.counterpp.commons.FolderUtils;
import com.bdzapps.counterpp.commons.ItemOffsetDecoration;
import com.bdzapps.counterpp.commons.Utils;
import com.bdzapps.counterpp.counterlist.CounterListActivity;
import com.bdzapps.counterpp.data.model.Folder;
import com.bdzapps.counterpp.folderstatistics.FolderStatisticsActivity;
import com.mbo.counter.R;

import java.util.ArrayList;
import java.util.List;

import static com.bdzapps.counterpp.counterlist.CounterListFragment.ARGUMENT_FOLDER_ID;
import static com.bdzapps.counterpp.counterlist.CounterListFragment.ARGUMENT_FOLDER_NAME;
import static com.bdzapps.counterpp.folderstatistics.FolderStatisticsFragment.ARGUMENT_STATISTICS_FOLDER_ID;

public class FolderListFragment extends Fragment implements FolderListContract.View
{
    private RecyclerView mFolderRecyclerView;
    private FolderListAdapter mRecyclerAdapter;
    private TextView mNoCounterTextView;
    private FolderListContract.Presenter mPresenter;
    private FolderItemListener mFolderListener = new FolderItemListener()
    {
        @Override
        public void onClick(Folder clickedFolder)
        {
            showCountersInFolder(clickedFolder);
        }

        @Override
        public void onFolderShowMenu(View view, final Folder clickedFolder, final int folderPosition)
        {
            if (getActivity() != null)
            {
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.inflate(R.menu.popup_menu_counter_group);
                if (clickedFolder.getCounters() == null || clickedFolder.getCounters().size() == 0)
                {
                    popup.getMenu().findItem(R.id.action_reset_all).setVisible(false);
                    popup.getMenu().findItem(R.id.action_delete_all).setVisible(false);
                    popup.getMenu().findItem(R.id.action_statistics).setVisible(false);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.action_rename:
                                renameFolder(folderPosition);
                                return true;
                            case R.id.action_reset_all:
                                Utils.showWarningDialog(getContext(), true,
                                        R.string.information, R.string.reset_all_counters_warning,
                                        new CallBack()
                                        {
                                            @Override
                                            public void execute(Object data)
                                            {
                                                mPresenter.resetCountersInFolder(clickedFolder.getId());
                                            }
                                        }, null, R.drawable.ic_info_24dp);
                                return true;
                            case R.id.action_delete_all:
                                Utils.showWarningDialog(getContext(), true,
                                        R.string.warning, R.string.delete_all_counters_warning,
                                        new CallBack()
                                        {
                                            @Override
                                            public void execute(Object data)
                                            {
                                                mPresenter.deleteCountersInGroup(clickedFolder.getId());
                                            }
                                        }, null, R.drawable.ic_info_24dp);
                                return true;
                            case R.id.action_duplicate:
                                mPresenter.duplicateFolder(clickedFolder.getId());
                                return true;
                            case R.id.action_delete:
                                Utils.showWarningDialog(getContext(), true,
                                        R.string.warning, R.string.delete_folder_warning,
                                        new CallBack()
                                        {
                                            @Override
                                            public void execute(Object data)
                                            {
                                                mPresenter.deleteFolder(clickedFolder.getId());
                                            }
                                        }, null, R.drawable.ic_info_24dp);
                                return true;
                            case R.id.action_statistics:
                                showFolderStatisticsUi(clickedFolder.getId());
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popup.show();
            }
        }

        @Override
        public void onItemMove(Folder folder)
        {
            mPresenter.saveFolder(folder);
        }
    };

    public static FolderListFragment newInstance()
    {
        return new FolderListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mRecyclerAdapter = new FolderListAdapter(new ArrayList<Folder>(0), mFolderListener);
        setHasOptionsMenu(true); // The fragment handle the menu
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.folder_list_fragment, container, false);
        mNoCounterTextView = root.findViewById(R.id.no_counter_text_view);
        // Set up counters view
        mFolderRecyclerView = root.findViewById(R.id.counter_recycler_view);
        mFolderRecyclerView.setAdapter(mRecyclerAdapter);
        mFolderRecyclerView.addItemDecoration(new ItemOffsetDecoration(container.getContext(), R.dimen.recycler_view_item_offset));
        mFolderRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        // Set up drag and drop
        ItemTouchHelper.Callback callback = new FolderListItemTouchHelperCallback(mRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mFolderRecyclerView);
        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add:
                FolderUtils.showAddFolder(getContext(), new CallBack()
                {
                    @Override
                    public void execute(Object data)
                    {
                        Folder folder = new Folder((String) data);
                        mPresenter.saveFolder(folder);
                    }
                });
                return true;
        }
        return false;
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        if (!hidden)
            mPresenter.loadFolders();
    }

    @Override
    public void setPresenter(FolderListContract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    @Override
    public void showCountersInFolder(Folder folder)
    {
        Intent intent = new Intent(getContext(), CounterListActivity.class);
        intent.putExtra(ARGUMENT_FOLDER_ID, folder.getId());
        intent.putExtra(ARGUMENT_FOLDER_NAME, folder.getName());
        startActivity(intent);
    }

    @Override
    public void showFolders(List<Folder> folders)
    {
        mFolderRecyclerView.setVisibility(View.VISIBLE);
        mNoCounterTextView.setVisibility(View.GONE);
        mRecyclerAdapter.replaceData(folders);
    }

    @Override
    public void showFolderStatisticsUi(int folderId)
    {
        Intent intent = new Intent(getContext(), FolderStatisticsActivity.class);
        intent.putExtra(ARGUMENT_STATISTICS_FOLDER_ID, folderId);
        startActivity(intent);
    }

    @Override
    public void showNoFolders()
    {
        mFolderRecyclerView.setVisibility(View.GONE);
        mNoCounterTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void renameFolder(final int folderPosition)
    {
        final Folder folder = (mRecyclerAdapter.getFolder(folderPosition));
        FolderUtils.showAddFolder(getContext(), folder.getName(), new CallBack()
        {
            @Override
            public void execute(Object data)
            {
                folder.setName((String) data);
                mPresenter.saveFolder(folder);
                mRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }
}
