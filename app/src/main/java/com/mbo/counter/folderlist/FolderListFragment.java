package com.mbo.counter.folderlist;

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

import com.mbo.counter.R;
import com.mbo.counter.commons.CallBack;
import com.mbo.counter.commons.FolderUtils;
import com.mbo.counter.commons.ItemOffsetDecoration;
import com.mbo.counter.counterlist.CounterListActivity;
import com.mbo.counter.data.model.Folder;

import java.util.ArrayList;
import java.util.List;

import static com.mbo.counter.counterlist.CounterListFragment.ARGUMENT_FOLDER_ID;
import static com.mbo.counter.counterlist.CounterListFragment.ARGUMENT_FOLDER_NAME;

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
                                mPresenter.resetCountersInGroup(clickedFolder.getId());
                                return true;
                            case R.id.action_delete_all:
                                mPresenter.deleteCountersInGroup(clickedFolder.getId());
                                return true;
                            case R.id.action_duplicate:
                                mPresenter.duplicateFolder(clickedFolder.getId());
                                return true;
                            case R.id.action_delete:
                                mPresenter.deleteFolder(clickedFolder.getId());
                                return true;
                            case R.id.action_statistics:
                                // TODO faire les statistics par groupe
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.inflate(R.menu.popup_menu_counter_group);
                popup.show();
            }
        }

        @Override
        public void onItemMove(Folder folder)
        {

        }
    };

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
                        mRecyclerAdapter.notifyDataSetChanged();
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

    public static FolderListFragment newInstance()
    {
        return new FolderListFragment();
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
    public void showNoFolders()
    {
        mFolderRecyclerView.setVisibility(View.GONE);
        mNoCounterTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void renameFolder(final int folderPosition)
    {
        final Folder folder = ((Folder) mRecyclerAdapter.getFolder(folderPosition));
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
